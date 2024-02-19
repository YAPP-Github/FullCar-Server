package com.fullcar.member.application.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcar.core.config.jwt.JwtTokenProvider;
import com.fullcar.core.exception.NotFoundException;
import com.fullcar.core.exception.UnauthorizedException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.application.member.MemberMapper;
import com.fullcar.member.domain.auth.SocialId;
import com.fullcar.member.domain.auth.service.SocialIdService;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.domain.member.MemberRepository;
import com.fullcar.member.presentation.auth.dto.request.KakaoAuthRequestDto;
import com.fullcar.member.presentation.auth.dto.response.SocialInfoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoAuthService {
    @Value("${kakao.admin-key}")
    private String adminKey;
    private static final String KAKAO_UNLINK_ENDPOINT = "https://kapi.kakao.com/v1/user/unlink";
    private final JwtTokenProvider jwtTokenProvider;

    private final MemberRepository memberRepository;
    private final SocialIdService socialIdService;
    private final MemberMapper memberMapper;

    @Transactional
    public SocialInfoResponseDto getMemberInfo(KakaoAuthRequestDto kakaoAuthRequestDto) {
        SocialId socialId = socialIdService.generateSocialId(getKakaoData(kakaoAuthRequestDto.getToken()));
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        if (memberRepository.existsBySocialId(socialId)) memberRepository.findBySocialId(socialId).loginMember(refreshToken);
        else createMember(socialId, refreshToken);

        return SocialInfoResponseDto.builder()
                .socialId(socialId)
                .refreshToken(refreshToken)
                .build();
    }

    private static String getKakaoData(String kakaoToken) {

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

            return jsonNode.get("id").asText();
        } catch (HttpClientErrorException e) {
            throw new NotFoundException(ErrorCode.UNAUTHORIZED_KAKAO_TOKEN);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // 새로운 멤버 생성
    private void createMember(SocialId socialId, String refreshToken) {
        Member member = memberMapper.toKakaoLoginEntity(socialId, refreshToken);
        memberRepository.saveAndFlush(member);
    }

    // 회원 탈퇴
    public void revoke(Member member) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + adminKey);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("target_id_type", "user_id");
        map.add("target_id", member.getSocialId().toString());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            restTemplate.exchange(
                    KAKAO_UNLINK_ENDPOINT,
                    HttpMethod.POST,
                    request,
                    String.class);
        } catch (HttpClientErrorException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_KAKAO_USER);
        }
    }
}
