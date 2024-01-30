package com.fullcar.member.presentation.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyRequestDto {
    @Schema(description = "회사 이름")
    @NotBlank
    private String companyName;

    @Schema(description = "위도", example = "46.652719")
    private BigDecimal latitude;

    @Schema(description = "경도", example = "71.530045")
    private BigDecimal longitude;
}
