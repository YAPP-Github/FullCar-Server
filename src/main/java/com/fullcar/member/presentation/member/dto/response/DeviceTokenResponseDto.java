package com.fullcar.member.presentation.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeviceTokenResponseDto {
    @Schema(description = "디바이스 토큰", example = "123")
    private String deviceToken;
}
