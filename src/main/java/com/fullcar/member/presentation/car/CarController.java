package com.fullcar.member.presentation.car;

import com.fullcar.member.application.car.CarService;
import com.fullcar.member.presentation.car.dto.CarDto;
import com.fullcar.core.annotation.CurrentMember;
import com.fullcar.core.response.ApiResponse;
import com.fullcar.core.response.SuccessCode;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.presentation.car.dto.request.CarUpdateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cars")
@Tag(name = "[Car] 차량 관련 API")
public class CarController {
    private final CarService carService;

    @Operation(summary = "차량 등록 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @PostMapping()
    public ApiResponse<CarDto> postCar(@CurrentMember Member member, @RequestBody @Valid CarDto carDto) {
        CarDto responseDto = carService.registerCar(member, carDto);
        return ApiResponse.success(SuccessCode.REGISTER_SUCCESS, responseDto);
    }

    @Operation(summary = "차량 정보 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @GetMapping()
    public ApiResponse<CarDto> getCar(@CurrentMember Member member) {
        CarDto responseDto = carService.getCar(member);
        return ApiResponse.success(SuccessCode.READ_SUCCESS, responseDto);
    }

    @Operation(summary = "차량 정보 수정 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @PatchMapping()
    public ApiResponse<Object> updateCar(@CurrentMember Member member, @RequestBody @Valid CarUpdateRequestDto carUpdateRequestDto) {
        carService.updateCar(member, carUpdateRequestDto);
        return ApiResponse.success(SuccessCode.UPDATE_SUCCESS);
    }
}
