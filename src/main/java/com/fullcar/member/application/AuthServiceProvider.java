package com.fullcar.member.application;

import com.fullcar.core.config.jwt.JwtTokenProvider;
import com.fullcar.core.config.jwt.UserAuthentication;
import com.fullcar.member.domain.Member;
import com.fullcar.member.domain.MemberRepository;
import com.fullcar.member.domain.MemberSocialType;
import com.fullcar.member.presentation.dto.response.AuthResponseDto;
import com.fullcar.member.presentation.dto.response.SocialInfoResponseDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthServiceProvider {
    private static final Map<MemberSocialType, AuthService> authServiceMap = new HashMap<>();

    private final KakaoAuthService kakaoAuthService;
    private final AppleAuthService appleAuthService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @PostConstruct
    void initializeAuthServicesMap() {
        authServiceMap.put(MemberSocialType.KAKAO, kakaoAuthService);
        authServiceMap.put(MemberSocialType.APPLE, appleAuthService);
    }

    public AuthService getAuthService(MemberSocialType socialType) {
        return authServiceMap.get(socialType);
    }

    public AuthResponseDto socialLogin(SocialInfoResponseDto socialResponseDto) {

        Member member = memberRepository.findBySocialIdAndIsDeleted(socialResponseDto.getSocialId(), false);
        Authentication authentication = new UserAuthentication(member.getId(), null, null);
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);

        return AuthResponseDto.builder()
                .flag(member.isFlag())
                .accessToken(accessToken)
                .refreshToken(socialResponseDto.getRefreshToken())
                .build();
    }
}
