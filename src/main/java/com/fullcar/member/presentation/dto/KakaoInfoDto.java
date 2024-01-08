package com.fullcar.member.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoInfoDto {
    private long clientId;

    private String gender;

    private String ageRange;
}
