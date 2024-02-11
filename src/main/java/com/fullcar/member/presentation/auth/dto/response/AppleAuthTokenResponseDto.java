package com.fullcar.member.presentation.auth.dto.response;

import lombok.Getter;

@Getter
public class AppleAuthTokenResponseDto {
    private String accessToken;
    private Integer expiresIn;
    private String idToken;
    private String refreshToken;
    private String tokenType;
}
