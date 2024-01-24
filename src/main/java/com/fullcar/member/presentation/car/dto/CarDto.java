package com.fullcar.member.presentation.car.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CarDto {
    @Schema(description = "차량 id")
    private Long id;

    @Schema(description = "차량번호", example = "23루 1234")
    @NotBlank
    private String carNo;

    @Schema(description = "차량명", example = "펠리세이드")
    @NotBlank
    private String carName;

    @Schema(description = "브랜드", example = "현대")
    @NotBlank
    private String carBrand;

    @Schema(description = "색상", example = "화이트")
    @NotBlank
    private String carColor;
}
