package com.fullcar.member.application.member;

import com.fullcar.member.domain.auth.SocialId;
import com.fullcar.member.domain.member.Company;
import com.fullcar.member.domain.member.service.MemberIdService;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.presentation.member.dto.request.OnboardingRequestDto;
import com.fullcar.member.presentation.member.dto.response.MemberGetResponseDto;
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
                .companyName(member.getCompany().getCompanyName())
                .email(member.getEmail())
                .gender(member.getGender())
                .carId(member.getCarId())
                .build();
    }

    public Member toEntity(OnboardingRequestDto onboardingRequestDto) {
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

    public Member toLoginEntity(SocialId socialId, String deviceToken, String refreshToken) {
        return Member.builder()
                .id(memberIdService.nextId())
                .socialId(socialId)
                .deviceToken(deviceToken)
                .refreshToken(refreshToken)
                .build();
    }
}
