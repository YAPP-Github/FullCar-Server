package com.fullcar.carpool.presentation.form.dto.response;

import com.fullcar.carpool.domain.carpool.PeriodType;
import com.fullcar.carpool.domain.form.FormState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "신청서 응답 모델")
public class FormResponseDto {
    @Schema(description = "신청서 id")
    private Long id;

    @Schema(description = "픽업장소")
    private String pickupLocation;

    @Schema(description = "희망비용(기간)")
    private PeriodType periodType;

    @Schema(description = "희망비용(가격)")
    private Long money;

    @Schema(description = "운전자에게 전할 말")
    private String content;

    @Schema(description = "신청서 상태")
    private FormState formState;

    @Schema(description = "회사명")
    private String companyName;

    @Schema(description = "닉네임")
    private String nickname;

    @Schema(description = "생성 Timestamp")
    private LocalDateTime createdAt;
}
