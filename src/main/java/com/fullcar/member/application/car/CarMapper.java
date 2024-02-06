package com.fullcar.member.application.car;

import com.fullcar.member.domain.car.Car;
import com.fullcar.member.domain.car.service.CarIdService;
import com.fullcar.member.presentation.car.dto.request.CarRequestDto;
import com.fullcar.member.presentation.car.dto.response.CarResponseDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CarMapper {
    private final CarIdService carIdService;

    public CarResponseDto toDto(Car car) {
        return CarResponseDto.builder()
                .id(car.getCarId().getId())
                .carNo(car.getCarNo())
                .carBrand(car.getCarBrand())
                .carName(car.getCarName())
                .carColor(car.getCarColor())
                .build();
    }

    public Car toEntity(CarRequestDto carRequestDto) {
        return Car.builder()
                .carId(carIdService.nextId())
                .carNo(carRequestDto.getCarNo())
                .carBrand(carRequestDto.getCarBrand())
                .carName(carRequestDto.getCarName())
                .carColor(carRequestDto.getCarColor())
                .build();
    }
}
