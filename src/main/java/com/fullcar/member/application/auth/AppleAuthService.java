package com.fullcar.member.application.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcar.core.config.jwt.JwtTokenProvider;
import com.fullcar.core.exception.BadRequestException;
import com.fullcar.core.exception.UnauthorizedException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.application.member.MemberMapper;
import com.fullcar.member.domain.auth.SocialId;
import com.fullcar.member.domain.auth.service.SocialIdService;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.domain.member.MemberRepository;
import com.fullcar.member.presentation.auth.dto.request.AppleAuthRequestDto;
import com.fullcar.member.presentation.auth.dto.response.AppleAuthTokenResponseDto;
import com.fullcar.member.presentation.auth.dto.response.SocialInfoResponseDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.bouncycastle.openssl.PEMParser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.ZonedDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class AppleAuthService {

    @Value("${apple.team-id}")
    private String teamId;

    @Value("${apple.key-id}")
    private String keyId;

    @Value("${apple.client-id}")
    private String clientId;

    private static final String REQUEST_TOKEN_URL = "https://appleid.apple.com/auth/token";
    private static final String REVOKE_TOKEN_URL = "https://appleid.apple.com/auth/revoke";

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final SocialIdService socialIdService;

    @Transactional
    public SocialInfoResponseDto getMemberInfo(AppleAuthRequestDto appleAuthRequestDto) throws IOException {
        String idToken = appleAuthRequestDto.getIdToken();
        String appleRefreshToken = requestAppleAuthToken(appleAuthRequestDto.getAuthCode()).getRefreshToken();

        Map<String, String> headers = parseHeaders(idToken);
        ApplePublicKeyList applePublicKeys = getApplePublicKeyList();
        PublicKey publicKey = generatePublicKey(headers, applePublicKeys);

        Claims claims = extractClaims(idToken, publicKey);
        validateClaims(claims);

        String sub = claims.getSubject();
        SocialId socialId = socialIdService.generateSocialId(sub);
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        if (memberRepository.existsBySocialId(socialId)) {
            Member member = memberRepository.findBySocialId(socialId);
            member.saveAppleRefreshToken(appleRefreshToken);
            member.loginMember(refreshToken);
        }
        else createMember(socialId, appleRefreshToken, refreshToken);

        return SocialInfoResponseDto.builder()
                .socialId(socialId)
                .refreshToken(refreshToken)
                .build();
    }

    // 새로운 멤버 생성
    private void createMember(SocialId socialId, String authCode, String refreshToken) {
        Member member = memberMapper.toAppleLoginEntity(socialId, authCode, refreshToken);
        memberRepository.saveAndFlush(member);
    }

    // Claim 검증
    private void validateClaims(Claims claims) {
        if (!claims.getIssuer().contains("https://appleid.apple.com") || !claims.getAudience().equals(clientId)) {
            throw new UnauthorizedException(ErrorCode.INVALID_CLAIMS);
        }
    }

    // id_token 헤더 추출
    private Map<String, String> parseHeaders(String idToken) {
        try {
            String encodedHeader = idToken.split("\\.")[0];
            String decodedHeader = new String(Base64.getDecoder().decode(encodedHeader));
            return objectMapper.readValue(decodedHeader, Map.class);
        } catch (JsonProcessingException | ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException(e);
        }
    }

    // id_token claim 추출
    private Claims extractClaims(String idToken, PublicKey publicKey) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey).build()
                    .parseClaimsJws(idToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtException(ErrorCode.EXPIRED_TOKEN.getMessage());
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            throw new JwtException(ErrorCode.UNAUTHORIZED_TOKEN.getMessage());
        }
    }

    // apple public key 정보 가져오기
    private ApplePublicKeyList getApplePublicKeyList() {
        RestTemplate restTemplate = new RestTemplate();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/json");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

            HttpEntity<MultiValueMap<String, String>> applePublicKeys =
                    new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    "https://appleid.apple.com/auth/keys",
                    HttpMethod.GET,
                    applePublicKeys,
                    String.class
            );

            return objectMapper.readValue(response.getBody(), ApplePublicKeyList.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // RSA 공개키 생성
    private PublicKey generatePublicKey(Map<String, String> header, ApplePublicKeyList keys) {
        ApplePublicKey publicKey = keys.getMatchesKey(header.get("alg"), header.get("kid"));

        byte[] nBytes = Base64.getUrlDecoder().decode(publicKey.getN());
        byte[] eBytes = Base64.getUrlDecoder().decode(publicKey.getE());

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(publicKey.getKty());
            return keyFactory.generatePublic(publicKeySpec);
        } catch (Exception exception) {
            throw new BadRequestException(ErrorCode.FAILED_TO_GENERATE_PUBLIC_KEY);
        }
    }

    private String createClientSecret() throws IOException {
        Date expirationDate = Date.from(ZonedDateTime.now().plusDays(30).toInstant());
        Map<String, Object> jwtHeader = new HashMap<>();
        jwtHeader.put("kid", keyId);
        jwtHeader.put("alg", "ES256");

        return Jwts.builder()
                .setHeaderParams(jwtHeader)
                .setIssuer(teamId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .setAudience("https://appleid.apple.com")
                .setSubject(clientId)
                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource("private_key.p8");
        String privateKey = new String(Files.readAllBytes(Paths.get(resource.getURI())));
        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        return converter.getPrivateKey(object);
    }

    public AppleAuthTokenResponseDto requestAppleAuthToken(String code) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", createClientSecret());
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<AppleAuthTokenResponseDto> response = restTemplate.postForEntity(REQUEST_TOKEN_URL, httpEntity, AppleAuthTokenResponseDto.class);
            return response.getBody();
        } catch (Exception e) {
            throw new UnauthorizedException(ErrorCode.FAILED_TO_GENERATE_APPLE_REFRESH_TOKEN);
        }
    }

    // 회원 탈퇴
    public void revoke(Member member) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", createClientSecret());
        params.add("token", member.getAppleRefreshToken());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        restTemplate.postForEntity(REVOKE_TOKEN_URL, httpEntity, AppleAuthTokenResponseDto.class);
    }
}
