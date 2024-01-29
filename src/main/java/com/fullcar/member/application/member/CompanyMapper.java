package com.fullcar.member.application.member;

import com.fullcar.member.domain.member.Company;
import com.fullcar.member.presentation.member.dto.request.CompanyRequestDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyMapper {
    public Company toEntity(CompanyRequestDto companyRequestDto) {
        return Company.builder()
                .companyName(companyRequestDto.getCompanyName())
                .latitude(companyRequestDto.getLatitude())
                .longitude(companyRequestDto.getLongitude())
                .build();
    }
}
