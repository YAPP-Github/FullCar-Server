package com.fullcar.member.presentation.auth.dto.response;

import com.fullcar.member.domain.auth.SocialId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocialInfoResponseDto {
    private SocialId socialId;
    private String refreshToken;
}
