package com.fullcar.member.application.member;

import com.fullcar.member.domain.auth.SocialId;
import com.fullcar.member.domain.member.Company;
import com.fullcar.member.domain.member.SocialType;
import com.fullcar.member.domain.member.service.MemberIdService;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.presentation.member.dto.request.OnBoardingRequestDto;
import com.fullcar.member.presentation.member.dto.response.MemberGetResponseDto;
import com.fullcar.member.presentation.member.dto.response.OnBoardingResponseDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberMapper {

    private final MemberIdService memberIdService;

    public MemberGetResponseDto toDto(Member member) {
        return MemberGetResponseDto.builder()
                .nickname(member.getNickname())
                .companyName(member.getCompany() == null ? null : member.getCompany().getCompanyName())
                .email(member.getEmail())
                .gender(member.getGender())
                .carId(member.getCarId())
                .build();
    }

    public OnBoardingResponseDto toOnBoardingDto(Member member) {
        return OnBoardingResponseDto.builder()
                .nickname(member.getNickname())
                .companyName(member.getCompany().getCompanyName())
                .latitude(member.getCompany().getLatitude())
                .longitude(member.getCompany().getLongitude())
                .email(member.getEmail())
                .gender(member.getGender())
                .build();
    }

    public Member toEntity(OnBoardingRequestDto onboardingRequestDto) {
        return Member.builder()
                .company(
                        Company.builder()
                                .companyName(onboardingRequestDto.getCompanyName())
                                .latitude(onboardingRequestDto.getLatitude())
                                .longitude(onboardingRequestDto.getLongitude())
                                .build()
                )
                .email(onboardingRequestDto.getEmail())
                .nickname(onboardingRequestDto.getNickname())
                .gender(onboardingRequestDto.getGender())
                .build();
    }

    public Member toKakaoLoginEntity(SocialId socialId, String refreshToken) {
        return Member.builder()
                .id(memberIdService.nextId())
                .socialId(socialId)
                .refreshToken(refreshToken)
                .socialType(SocialType.KAKAO)
                .build();
    }

    public Member toAppleLoginEntity(SocialId socialId, String appleRefreshToken, String refreshToken) {
        return Member.builder()
                .id(memberIdService.nextId())
                .socialId(socialId)
                .appleRefreshToken(appleRefreshToken)
                .refreshToken(refreshToken)
                .socialType(SocialType.APPLE)
                .build();
    }
}
