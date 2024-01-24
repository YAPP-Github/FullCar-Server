package com.fullcar.member.application.car;

import com.fullcar.member.domain.car.Car;
import com.fullcar.member.domain.car.service.CarIdService;
import com.fullcar.member.presentation.car.dto.CarDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CarMapper {
    private final CarIdService carIdService;

    public CarDto toDto(Car car) {
        return CarDto.builder()
                .id(car.getCarId().getId())
                .carNo(car.getCarNo())
                .carBrand(car.getCarBrand())
                .carName(car.getCarName())
                .carColor(car.getCarColor())
                .build();
    }

    public Car toEntity(CarDto carDto) {
        return Car.builder()
                .carId(carIdService.nextId())
                .carNo(carDto.getCarNo())
                .carBrand(carDto.getCarBrand())
                .carName(carDto.getCarName())
                .carColor(carDto.getCarColor())
                .build();
    }
}
