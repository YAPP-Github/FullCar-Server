package com.fullcar.carpool.presentation;

import com.fullcar.carpool.application.CarpoolService;
import com.fullcar.carpool.presentation.dto.CarpoolDto;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(description = "카풀 관련 Endpoint", name = "카풀")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carpools")
public class CarpoolController {
    private final CarpoolService carpoolService;

    @Operation(description = "카풀 등록")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "등록 성공")
    })
    @PostMapping("")
    public ApiResponse<CarpoolDto> postCarpool(
            @Parameter(hidden = true)
            @CurrentMember Member member,
            @Parameter(description = "카풀 모델", required = true)
            @RequestBody @Valid CarpoolDto carpoolDto
    ) {
        return ApiResponse.success(
                SuccessCode.REGISTER_SUCCESS,
                carpoolService.registerCarpool(member.getId(), carpoolDto)
        );
    }
}
