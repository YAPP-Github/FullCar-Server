package com.fullcar.carpool.presentation.form;

import com.fullcar.carpool.application.form.FormService;
import com.fullcar.carpool.domain.carpool.CarpoolId;
import com.fullcar.carpool.presentation.form.dto.request.FormRequestDto;
import com.fullcar.carpool.presentation.form.dto.response.FormResponseDto;
import com.fullcar.core.annotation.CurrentMember;
import com.fullcar.core.response.ApiResponse;
import com.fullcar.core.response.SuccessCode;
import com.fullcar.member.domain.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[Form] 신청서 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carpools/{carpoolId}/forms")
public class FormController {
    private final FormService formService;

    @Operation(summary = "신청서 등록 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "등록 성공")
    })
    @PostMapping("")
    public ApiResponse<FormResponseDto> postForm(
            @Parameter(hidden = true)
            @CurrentMember Member member,
            @Parameter(description = "카풀 id", required = true)
            @PathVariable Long carpoolId,
            @Parameter(description = "신청서 요청모델", required = true)
            @RequestBody @Valid FormRequestDto formRequestDto
    ) {
        return ApiResponse.success(
                SuccessCode.REGISTER_SUCCESS,
                formService.requestForm(
                        member,
                        new CarpoolId(carpoolId),
                        formRequestDto
                )
        );
    }
}
