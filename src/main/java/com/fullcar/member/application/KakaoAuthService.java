package com.fullcar.member.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcar.core.config.jwt.JwtTokenProvider;
import com.fullcar.core.config.jwt.UserAuthentication;
import com.fullcar.core.exception.NotFoundException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.domain.Member;
import com.fullcar.member.domain.MemberIdService;
import com.fullcar.member.domain.MemberRepository;
import com.fullcar.member.presentation.dto.KakaoInfoDto;
import com.fullcar.member.presentation.dto.request.AuthRequestDto;
import com.fullcar.member.presentation.dto.response.AuthResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoAuthService implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;

    private final MemberRepository memberRepository;
    private final MemberIdService memberIdService;

    public static KakaoInfoDto getKakaoData(String kakaoToken) {

        try {
            HttpHeaders headers = new HttpHeaders();

            headers.add("Authorization", "Bearer " + kakaoToken);
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    kakaoUserInfoRequest,
                    String.class
            );

            String responseBody = response.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            JsonNode kakaoAccount = jsonNode.get("kakao_account");

            long clientId = jsonNode.get("id").asLong();
            String gender = kakaoAccount.get("gender").asText();
            String ageRange = kakaoAccount.get("age_range").asText();

            return new KakaoInfoDto(clientId, gender, ageRange);
        } catch (HttpClientErrorException e) {
            throw new NotFoundException(ErrorCode.UNAUTHORIZED_KAKAO_TOKEN);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthResponseDto socialLogin(AuthRequestDto authRequestDto) {

        KakaoInfoDto kakaoInfoDto = login(authRequestDto.getAccessToken());
        long clientId = kakaoInfoDto.getClientId();
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        Member member = Member.builder()
                .id(memberIdService.nextId())
                .clientId(kakaoInfoDto.getClientId())
                .gender(kakaoInfoDto.getGender())
                .ageRange(kakaoInfoDto.getAgeRange())
                .flag(false)
                .isDeleted(false)
                .fcmToken(authRequestDto.getFcmToken())
                .refreshToken(refreshToken)
                .build();

            memberRepository.save(member);

        // 등록된 유저 찾기
        Member signedMember = findMemberByClientId(clientId);

        Authentication authentication = new UserAuthentication(signedMember.getId(), null, null);
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);

        return AuthResponseDto.builder()
                .flag(signedMember.isFlag())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
}

    private Member findMemberByClientId(long clientId) {
        return memberRepository.findByClientIdAndIsDeleted(clientId, false)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private KakaoInfoDto login(String kakaoAccessToken) {
        return KakaoAuthService.getKakaoData(kakaoAccessToken);
    }
}
