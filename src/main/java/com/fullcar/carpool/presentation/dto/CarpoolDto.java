package com.fullcar.carpool.presentation.dto;

import com.fullcar.carpool.domain.Carpool;
import com.fullcar.carpool.domain.CarpoolId;
import com.fullcar.carpool.domain.MoodType;
import com.fullcar.carpool.domain.PeriodType;
import com.fullcar.carpool.domain.service.CarpoolIdService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.stereotype.Component;


@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "카풀 모델")
public class CarpoolDto {

    @Schema(description = "카풀 id")
    private Long id;

    @Schema(description = "픽업장소")
    private String pickupLocation;

    @Schema(description = "희망비용(기간)")
    private PeriodType periodType;

    @Schema(description = "희망비용(가격)")
    private Long money;

    @Schema(description = "탑승자에게 전할 말")
    private String content;

    @Schema(description = "운행 분위기")
    private MoodType moodType;
}
