package com.fullcar.member.presentation.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AuthTokenRequestDto {

    @Schema(description = "리프레쉬 토큰")
    private String refreshToken;
}
