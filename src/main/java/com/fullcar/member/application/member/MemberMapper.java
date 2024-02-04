package com.fullcar.member.application.member;

import com.fullcar.member.infra.EmailMessage;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.presentation.member.dto.request.EmailRequestDto;
import com.fullcar.member.presentation.member.dto.response.MemberGetResponseDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberMapper {

    public MemberGetResponseDto toDto(Member member) {
        return MemberGetResponseDto.builder()
                .nickname(member.getNickname())
                .companyName(member.getCompany().getCompanyName())
                .email(member.getEmail())
                .gender(member.getGender())
                .carId(member.getCarId())
                .build();
    }

    public EmailMessage toEntity(EmailRequestDto emailRequestDto) {
        return EmailMessage.builder()
                .to(emailRequestDto.getEmail())
                .subject("[FullCar] 회사 이메일 인증")
                .build();
    }
}
