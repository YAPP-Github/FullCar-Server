package com.fullcar.carpool.presentation.dto.response;

import com.fullcar.carpool.domain.CarpoolState;
import com.fullcar.carpool.domain.MoodType;
import com.fullcar.carpool.domain.PeriodType;
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
@Schema(description = "카풀 응답 모델")
public class CarpoolResponseDto {

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

    @Schema(description = "카풀 상태")
    private CarpoolState carpoolState;

    @Schema(description = "회사명")
    private String companyName;

    @Schema(description = "성별")
    private String gender;

    @Schema(description = "생성 Timestamp")
    private LocalDateTime createdAt;

    @Getter
    @SuperBuilder
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Schema(description = "카풀 응답 모델")
    public static class CarpoolDetailDTO extends CarpoolResponseDto {

        @Schema(description = "닉네임")
        private String nickname;

        @Schema(description = "차량번호")
        private String carNo;

        @Schema(description = "차량명")
        private String carName;

        @Schema(description = "차량 브랜드")
        private String carBrand;

        @Schema(description = "차량 색상")
        private String carColor;
    }
}
