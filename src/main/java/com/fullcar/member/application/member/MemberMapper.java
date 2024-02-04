package com.fullcar.member.application.member;

import com.fullcar.member.domain.member.Member;
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
}
