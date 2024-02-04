package com.fullcar.member.presentation.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "회원 정보 응답 모델")
public class MemberGetResponseDto {

    @Schema(description = "회원 닉네임", example = "피곤한 물개")
    private String nickname;

    @Schema(description = "회사명", example = "현대 자동차")
    private String companyName;
}
