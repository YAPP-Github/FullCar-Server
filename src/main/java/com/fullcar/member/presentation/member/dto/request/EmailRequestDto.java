package com.fullcar.member.presentation.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailRequestDto {
    @Schema(description = "회사 이메일", example = "whoareyou@yanolja.com")
    private String email;
}
