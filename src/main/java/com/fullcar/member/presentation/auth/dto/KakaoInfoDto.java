package com.fullcar.member.presentation.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoInfoDto {
    private String socialId;

    private String gender;

    private String ageRange;
}
