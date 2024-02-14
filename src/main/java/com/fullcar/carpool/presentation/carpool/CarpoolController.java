package com.fullcar.carpool.presentation.carpool;

import com.fullcar.carpool.application.carpool.CarpoolService;
import com.fullcar.carpool.domain.carpool.CarpoolId;
import com.fullcar.carpool.presentation.carpool.dto.request.CarpoolRequestDto;
import com.fullcar.carpool.presentation.carpool.dto.response.CarpoolResponseDto;
import com.fullcar.carpool.presentation.carpool.dto.response.MyCarpoolDto;
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

import java.util.List;


@Tag(name = "[Carpool] 카풀 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CarpoolController {
    private final CarpoolService carpoolService;

    @Operation(summary = "카풀 등록 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "등록 성공")
    })
    @PostMapping("/carpools")
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
    @GetMapping("/carpools")
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
    @GetMapping("/carpools/{carpoolId}")
    public ApiResponse<CarpoolResponseDto.CarpoolDetailDtO> getCarpool(
            @Parameter(hidden = true)
            @CurrentMember Member member,
            @PathVariable Long carpoolId
    ) {
        return ApiResponse.success(
                SuccessCode.READ_SUCCESS,
                carpoolService.getCarpool(member, new CarpoolId(carpoolId))
        );
    }

    @Operation(summary = "내 카풀 목록 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/my-carpools")
    public ApiResponse<List<MyCarpoolDto>> getMyCarpools(
            @Parameter(hidden = true)
            @CurrentMember Member member
    ) {
        return ApiResponse.success(
                SuccessCode.READ_SUCCESS,
                carpoolService.getMyCarpoolList(member)
        );
    }

    @Operation(summary = "카풀 마감 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공")
    })
    @PatchMapping("/carpools/{carpoolId}")
    public ApiResponse<CarpoolResponseDto.CarpoolDetailDtO> patchCarpool(
            @Parameter(hidden = true)
            @CurrentMember Member member,
            @PathVariable Long carpoolId
    ) {
        return ApiResponse.success(
                SuccessCode.UPDATE_SUCCESS,
                carpoolService.closeCarpool(member, new CarpoolId(carpoolId))
        );
    }
}
