package com.fullcar.member.application.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcar.core.config.jwt.JwtTokenProvider;
import com.fullcar.core.exception.BadRequestException;
import com.fullcar.core.exception.UnauthorizedException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.domain.auth.SocialId;
import com.fullcar.member.domain.auth.service.SocialIdService;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.domain.member.service.MemberIdService;
import com.fullcar.member.domain.member.MemberRepository;
import com.fullcar.member.presentation.auth.dto.request.AuthRequestDto;
import com.fullcar.member.presentation.auth.dto.response.SocialInfoResponseDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AppleAuthService implements AuthService {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final MemberIdService memberIdService;
    private final SocialIdService socialIdService;
    private final AppleProperties appleProperties;

    @Override
    @Transactional
    public SocialInfoResponseDto getMemberInfo(AuthRequestDto authRequestDto) {
        String deviceToken = authRequestDto.getDeviceToken();
        String idToken = authRequestDto.getToken();
        Map<String, String> headers = parseHeaders(idToken);
        ApplePublicKeyList applePublicKeys = getApplePublicKeyList();
        PublicKey publicKey = generatePublicKey(headers, applePublicKeys);

        Claims claims = extractClaims(idToken, publicKey);
        validateClaims(claims);

        String sub = claims.getSubject();
        SocialId socialId = socialIdService.generateSocialId(sub);
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        if (memberRepository.existsBySocialId(socialId)) {
            memberRepository.findBySocialIdAndIsDeleted(socialId, false).loginMember(deviceToken, refreshToken);
        }
        else createMember(socialId, deviceToken, refreshToken);

        return SocialInfoResponseDto.builder()
                .socialId(socialId)
                .refreshToken(refreshToken)
                .build();
    }

    // 새로운 멤버 생성
    private void createMember(SocialId socialId, String deviceToken, String refreshToken) {
        memberRepository.save(Member.builder()
                .id(memberIdService.nextId())
                .socialId(socialId)
                .deviceToken(deviceToken)
                .refreshToken(refreshToken)
                .build());
    }

    // Claim 검증
    private void validateClaims(Claims claims) {
        if (!claims.getIssuer().contains(appleProperties.getIss()) || !claims.getAudience().equals(appleProperties.getClientId())) {
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
        try {
            RestTemplate restTemplate = new RestTemplate();

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
}
