package com.fullcar.member.presentation.dto.response;

import com.fullcar.member.domain.SocialId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocialInfoResponseDto {
    private SocialId socialId;
    private String refreshToken;
}
