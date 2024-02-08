package com.fullcar.carpool.presentation.form;

import com.fullcar.carpool.application.form.FormService;
import com.fullcar.carpool.domain.carpool.CarpoolId;
import com.fullcar.carpool.domain.form.FormId;
import com.fullcar.carpool.presentation.form.dto.request.FormRequestDto;
import com.fullcar.carpool.presentation.form.dto.request.FormUpdateDto;
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

import java.util.List;

@Tag(name = "[Form] 신청서 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FormController {
    private final FormService formService;

    @Operation(summary = "신청서 등록 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "등록 성공")
    })
    @PostMapping("/carpools/{carpoolId}/forms")
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

    @Operation(summary = "보낸 신청서 목록 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/sent-forms")
    public ApiResponse<List<FormResponseDto>> getSentFormList(
            @Parameter(hidden = true)
            @CurrentMember Member member
    ) {
        return ApiResponse.success(
                SuccessCode.READ_SUCCESS,
                formService.readSentFormList(member)
        );
    }

    @Operation(summary = "받은 신청서 목록 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/received-forms")
    public ApiResponse<List<FormResponseDto>> getReceivedFormList(
            @Parameter(hidden = true)
            @CurrentMember Member member
    ) {
        return ApiResponse.success(
                SuccessCode.READ_SUCCESS,
                formService.readReceivedFormList(member)
        );
    }

    @Operation(summary = "신청서 상세 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/forms/{formId}")
    public ApiResponse<FormResponseDto.FormDetailDto> getForm(
            @Parameter(description = "신청서 id", required = true)
            @PathVariable Long formId
    ) {
        return ApiResponse.success(
                SuccessCode.READ_SUCCESS,
                formService.readForm(new FormId(formId))
        );
    }

    @Operation(summary = "신청서 상태변경 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공")
    })
    @PatchMapping("/forms/{formId}")
    public ApiResponse<FormResponseDto.FormDetailDto> patchForm(
            @Parameter(hidden = true)
            @CurrentMember Member member,
            @Parameter(description = "신청서 id", required = true)
            @PathVariable Long formId,
            @Parameter(description = "신청서 상태변경 모델", required = true)
            @RequestBody FormUpdateDto formUpdateDto
            ) {
        return ApiResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                formService.updateForm(member, new FormId(formId), formUpdateDto)
        );
    }
}
