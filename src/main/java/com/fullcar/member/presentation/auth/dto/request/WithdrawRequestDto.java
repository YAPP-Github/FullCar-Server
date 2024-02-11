package com.fullcar.member.presentation.auth.dto.request;

import com.fullcar.member.domain.member.MemberSocialType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WithdrawRequestDto {
    @Schema(description = "소셜 로그인 타입", example = "APPLE")
    private MemberSocialType socialType;
}
