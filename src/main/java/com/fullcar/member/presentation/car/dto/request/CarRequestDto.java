package com.fullcar.member.presentation.car.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CarRequestDto {

    @Schema(description = "차량번호", example = "23루 1234")
    private String carNo;

    @Schema(description = "차량명", example = "펠리세이드")
    private String carName;

    @Schema(description = "브랜드", example = "현대")
    private String carBrand;

    @Schema(description = "색상", example = "화이트")
    private String carColor;
}
