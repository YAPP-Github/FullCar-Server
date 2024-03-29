package com.fullcar.member.presentation.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDto {

    @Schema(description = "FullCar Access Token")
    private String accessToken;

    @Schema(description = "FullCar Refresh Token")
    private String refreshToken;
}
