package com.fullcar.carpool.presentation;

import com.fullcar.carpool.application.CarpoolService;
import com.fullcar.carpool.domain.CarpoolId;
import com.fullcar.carpool.presentation.dto.request.CarpoolRequestDto;
import com.fullcar.carpool.presentation.dto.response.CarpoolResponseDto;
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
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;



@Tag(name = "[Carpool] 카풀 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carpools")
public class CarpoolController {
    private final CarpoolService carpoolService;

    @Operation(summary = "카풀 등록 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "등록 성공")
    })
    @PostMapping("")
    public ApiResponse<CarpoolResponseDto> postCarpool(
            @Parameter(hidden = true)
            @CurrentMember Member member,
            @Parameter(description = "카풀 모델", required = true)
            @RequestBody @Valid CarpoolRequestDto carpoolRequestDto
    ) {
        return ApiResponse.success(
                SuccessCode.REGISTER_SUCCESS,
                carpoolService.registerCarpool(member, carpoolRequestDto)
        );
    }

    @Operation(summary = "카풀 목록 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("")
    public ApiResponse<Slice<CarpoolResponseDto>> getCarpools(
            @Parameter(hidden = true)
            @CurrentMember Member member,
            @RequestParam int page,
            @RequestParam int size
            ) {
        return ApiResponse.success(
                SuccessCode.READ_SUCCESS,
                carpoolService.getCarpoolList(member, page, size)
        );
    }

    @Operation(summary = "카풀 상세 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/{carpoolId}")
    public ApiResponse<CarpoolResponseDto.CarpoolDetailDTO> getCarpool(
            @Parameter(hidden = true)
            @CurrentMember Member member,
            @PathVariable Long carpoolId
    ) {
        return ApiResponse.success(
                SuccessCode.READ_SUCCESS,
                carpoolService.getCarpool(member, new CarpoolId(carpoolId))
        );
    }
}
