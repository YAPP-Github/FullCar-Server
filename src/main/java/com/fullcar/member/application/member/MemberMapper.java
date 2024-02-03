package com.fullcar.member.application.member;

import com.fullcar.member.domain.member.EmailMessage;
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
                .build();
    }

    public EmailMessage toEntity(EmailRequestDto emailRequestDto) {
        return EmailMessage.builder()
                .to(emailRequestDto.getEmail())
                .subject("[FullCar] 이메일 인증을 위한 인증 코드 발송")
                .build();
    }
}
