package com.fullcar.member.application.auth;

import com.fullcar.core.config.jwt.JwtExceptionType;
import com.fullcar.core.config.jwt.JwtTokenProvider;
import com.fullcar.core.exception.CustomException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.application.car.CarService;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.domain.member.MemberRepository;
import com.fullcar.member.domain.member.SocialType;
import com.fullcar.member.domain.member.service.MailService;
import com.fullcar.member.presentation.auth.dto.response.AuthResponseDto;
import com.fullcar.member.presentation.auth.dto.response.AuthTokenResponseDto;
import com.fullcar.member.presentation.auth.dto.response.SocialInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final AppleAuthService appleAuthService;
    private final KakaoAuthService kakaoAuthService;
    private final CarService carService;
    private final MailService mailService;

    public AuthResponseDto socialLogin(SocialInfoResponseDto socialResponseDto) {
        Member member = memberRepository.findBySocialId(socialResponseDto.getSocialId());
        String accessToken = jwtTokenProvider.generateAccessToken(member);

        return AuthResponseDto.builder()
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

    @Transactional
    public void socialLogout(Member member) {
        memberRepository.findByIdAndIsDeletedOrThrow(member.getId(), false).clearRefreshTokenAndDeviceToken();
        memberRepository.flush();
    }

    @Transactional
    public void withdrawMember(Member member) throws IOException {
        if (member.getSocialType() == SocialType.APPLE) {
            appleAuthService.revoke(member);
        }
        else if (member.getSocialType() == SocialType.KAKAO) {
            kakaoAuthService.revoke(member);
        }
        else {
            throw new CustomException(ErrorCode.INVALID_SOCIAL_TYPE);
        }

        carService.deleteCar(member.getCarId());
        mailService.deleteMail(member.getId());
        memberRepository.saveAndFlush(member.deleted());

        // TODO: 이벤트 기반으로 게시글 및 요청 처리
    }
}
