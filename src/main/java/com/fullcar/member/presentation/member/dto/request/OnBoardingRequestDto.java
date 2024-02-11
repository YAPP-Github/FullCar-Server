package com.fullcar.member.presentation.member.dto.request;

import com.fullcar.member.domain.member.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OnBoardingRequestDto {

    @Schema(description = "회사명", example = "구글 코리아")
    @NotBlank
    private String companyName;

    @Schema(description = "위도", example = "46.652719")
    private BigDecimal latitude;

    @Schema(description = "경도", example = "71.530045")
    private BigDecimal longitude;

    @Schema(description = "회사 이메일", example = "whoareyou@yanolja.com")
    private String email;

    @Schema(description = "닉네임", example = "피곤한 물개")
    private String nickname;

    @Schema(description = "성별", example = "NONE")
    private Gender gender;
}
