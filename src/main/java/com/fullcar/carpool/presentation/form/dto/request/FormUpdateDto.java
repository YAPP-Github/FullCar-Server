package com.fullcar.carpool.presentation.form.dto.request;

import com.fullcar.carpool.domain.form.FormState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "신청서 상태변경 모델")
public class FormUpdateDto {
    @Schema(description = "신청서 상태")
    FormState formState;

    @Schema(description = "연락처")
    String contact;

    @Schema(description = "탑승자에게 전할 말")
    String toPassenger;
}
