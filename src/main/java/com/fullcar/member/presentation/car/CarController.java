package com.fullcar.member.presentation.car;

import com.fullcar.member.application.car.CarService;
import com.fullcar.member.presentation.car.dto.CarDto;
import com.fullcar.core.annotation.CurrentMember;
import com.fullcar.core.response.ApiResponse;
import com.fullcar.core.response.SuccessCode;
import com.fullcar.member.domain.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cars")
@Tag(name = "[Car] 차량 관련 API")
public class CarController {
    private final CarService carService;

    @Operation(description = "차량 등록")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    @PostMapping()
    public ApiResponse<CarDto> postCar(@CurrentMember Member member, @RequestBody @Valid CarDto carDto) {
        CarDto responseDto = carService.registerCar(member.getId(), carDto);
        return ApiResponse.success(SuccessCode.REGISTER_SUCCESS, responseDto);
    }
}
