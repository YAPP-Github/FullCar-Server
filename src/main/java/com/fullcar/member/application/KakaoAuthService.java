package com.fullcar.member.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcar.core.config.jwt.JwtTokenProvider;
import com.fullcar.core.exception.NotFoundException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.domain.*;
import com.fullcar.member.presentation.dto.KakaoInfoDto;
import com.fullcar.member.presentation.dto.request.AuthRequestDto;
import com.fullcar.member.presentation.dto.response.SocialInfoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final SocialIdService socialIdService;

    @Override
    @Transactional
    public SocialInfoResponseDto getMemberInfo(AuthRequestDto authRequestDto) {
        String deviceToken = authRequestDto.getDeviceToken();
        KakaoInfoDto kakaoInfoDto = getKakaoData(authRequestDto.getToken());
        SocialId socialId = socialIdService.generateSocialId(kakaoInfoDto.getSocialId());
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        if (memberRepository.existsBySocialId(socialId)) memberRepository.findBySocialIdAndIsDeleted(socialId, false).loginMember(deviceToken, refreshToken);
        else createMember(kakaoInfoDto, deviceToken, refreshToken);

        return SocialInfoResponseDto.builder()
                .socialId(socialId)
                .refreshToken(refreshToken)
                .build();
    }

    private static KakaoInfoDto getKakaoData(String kakaoToken) {

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

            String socialId = jsonNode.get("id").asText();
            String gender = kakaoAccount.get("gender").asText();
            String ageRange = kakaoAccount.get("age_range").asText();

            return new KakaoInfoDto(socialId, gender, ageRange);
        } catch (HttpClientErrorException e) {
            throw new NotFoundException(ErrorCode.UNAUTHORIZED_KAKAO_TOKEN);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // 새로운 멤버 생성
    private void createMember(KakaoInfoDto kakaoInfoDto, String deviceToken, String refreshToken) {
        memberRepository.save(Member.builder()
                .id(memberIdService.nextId())
                .socialId(socialIdService.generateSocialId(kakaoInfoDto.getSocialId()))
                .gender(kakaoInfoDto.getGender())
                .ageRange(kakaoInfoDto.getAgeRange())
                .deviceToken(deviceToken)
                .refreshToken(refreshToken)
                .build());
    }
}
