package com.fullcar.carpool.presentation.carpool.dto.response;

import com.fullcar.carpool.domain.carpool.CarpoolState;
import com.fullcar.carpool.domain.carpool.PeriodType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "카풀 List 응답 모델")
public class MyCarpoolDto {
    @Schema(description = "카풀 id")
    private Long id;

    @Schema(description = "픽업장소")
    private String pickupLocation;

    @Schema(description = "희망비용(기간)")
    private PeriodType periodType;

    @Schema(description = "희망비용(가격)")
    private Long money;

    @Schema(description = "카풀 상태")
    private CarpoolState carpoolState;

    @Schema(description = "생성 Timestamp")
    private LocalDateTime createdAt;
}
