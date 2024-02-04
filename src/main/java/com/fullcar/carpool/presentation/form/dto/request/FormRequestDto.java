package com.fullcar.carpool.presentation.form.dto.request;

import com.fullcar.carpool.domain.carpool.PeriodType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "신청서 요청모델")
public class FormRequestDto {

    @Schema(description = "픽업장소")
    private String pickupLocation;

    @Schema(description = "희망비용(기간)")
    private PeriodType periodType;

    @Schema(description = "희망비용(가격)")
    private Long money;

    @Schema(description = "운전자에게 전할 말")
    private String content;
}
