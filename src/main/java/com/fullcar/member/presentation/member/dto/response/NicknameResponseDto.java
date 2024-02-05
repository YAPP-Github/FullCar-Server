package com.fullcar.member.presentation.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "닉네임 응답 모델")
public class NicknameResponseDto {
    @Schema(description = "닉네임", example = "오예")
    private String nickname;
}
