package com.fullcar.member.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDto {

    private Boolean flag;

    private String accessToken;

    private String refreshToken;
}
