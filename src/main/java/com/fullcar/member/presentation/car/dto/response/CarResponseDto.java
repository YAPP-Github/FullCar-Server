package com.fullcar.member.presentation.car.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "차량 응답 모델")
public class CarResponseDto {
    @Schema(description = "차량 id")
    private Long id;

    @Schema(description = "차량번호", example = "23루 1234")
    private String carNo;

    @Schema(description = "차량명", example = "펠리세이드")
    private String carName;

    @Schema(description = "브랜드", example = "현대")
    private String carBrand;

    @Schema(description = "색상", example = "화이트")
    private String carColor;
}
