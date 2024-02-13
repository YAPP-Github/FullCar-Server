package com.fullcar.member.presentation.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppleAuthTokenResponseDto {
    @JsonProperty("refresh_token")
    private String refreshToken;
}
