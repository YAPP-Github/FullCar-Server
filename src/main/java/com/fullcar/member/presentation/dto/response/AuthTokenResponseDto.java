package com.fullcar.member.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthTokenResponseDto {
    private String accessToken;

    private String refreshToken;
}
