package com.fullcar.member.application;

import com.fullcar.member.domain.MemberSocialType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthServiceProvider {
    private static final Map<MemberSocialType, AuthService> authServiceMap = new HashMap<>();

    private final KakaoAuthService kakaoAuthService;
    private final AppleAuthService appleAuthService;

    @PostConstruct
    void initializeAuthServicesMap() {
        authServiceMap.put(MemberSocialType.KAKAO, kakaoAuthService);
        // authServiceMap.put(MemberSocialType.APPLE, appleAuthService);
    }

    public AuthService getAuthService(MemberSocialType socialType) {
        return authServiceMap.get(socialType);
    }
}
