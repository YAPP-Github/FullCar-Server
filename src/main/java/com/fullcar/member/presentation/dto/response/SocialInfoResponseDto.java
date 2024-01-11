package com.fullcar.member.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocialInfoResponseDto {
    private String socialId;
    private String refreshToken;
}
