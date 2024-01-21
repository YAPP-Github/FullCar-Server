package com.fullcar.member.application;

import com.fullcar.core.config.jwt.JwtExceptionType;
import com.fullcar.core.config.jwt.JwtTokenProvider;
import com.fullcar.core.config.jwt.UserAuthentication;
import com.fullcar.core.exception.BadRequestException;
import com.fullcar.core.exception.CustomException;
import com.fullcar.core.exception.UnauthorizedException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.domain.Member;
import com.fullcar.member.domain.MemberRepository;
import com.fullcar.member.domain.MemberSocialType;
import com.fullcar.member.presentation.dto.response.AuthResponseDto;
import com.fullcar.member.presentation.dto.response.AuthTokenResponseDto;
import com.fullcar.member.presentation.dto.response.SocialInfoResponseDto;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

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
        String accessToken = jwtTokenProvider.generateAccessToken(member);

        return AuthResponseDto.builder()
                .onBoardingFlag(member.isFlag())
                .accessToken(accessToken)
                .refreshToken(socialResponseDto.getRefreshToken())
                .build();
    }

    public AuthTokenResponseDto getNewToken(String refreshToken) {
        // refresh 만료
        if (jwtTokenProvider.validateToken(refreshToken) == JwtExceptionType.EXPIRED_JWT_TOKEN) {
            throw new CustomException(ErrorCode.SIGNIN_REQUIRED);
        }

        // 해당 refreshToken을 가진 멤버가 존재하는지 확인
        Member member = memberRepository.findByRefreshTokenOrThrow(refreshToken);
        String newAccessToken = jwtTokenProvider.generateAccessToken(member);

        return AuthTokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
