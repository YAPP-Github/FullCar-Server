package com.fullcar.member.application.car;

import com.fullcar.core.exception.CustomException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.domain.car.Car;
import com.fullcar.member.domain.car.CarId;
import com.fullcar.member.domain.car.CarRepository;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.domain.member.MemberRepository;
import com.fullcar.member.presentation.car.dto.request.CarRequestDto;
import com.fullcar.member.presentation.car.dto.response.CarResponseDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CarService {
    private final CarRepository carRepository;
    private final MemberRepository memberRepository;
    private final CarMapper carMapper;

    @Transactional
    public CarResponseDto registerCar(Member member, CarRequestDto carRequestDto) {
        Car car = carMapper.toEntity(carRequestDto);

        if (memberRepository.findByIdAndIsDeletedOrThrow(member.getId(), false).getCarId() != null) {
            throw new CustomException(ErrorCode.EXISTED_CAR_IN_MEMBER);
        }

        memberRepository.saveAndFlush(member.addCarInformation(car.getCarId()));

        return carMapper.toDto(
                carRepository.saveAndFlush(car)
        );
    }

    @Transactional(readOnly = true)
    public CarResponseDto getCar(Member member) {
        Car car = carRepository.findByCarIdAndIsDeletedOrThrow(member.getCarId(), false);
        return carMapper.toDto(car);
    }

    @Transactional
    public void updateCar(Member member, CarRequestDto carRequestDto) {
        Car car = carRepository.findByCarIdAndIsDeletedOrThrow(member.getCarId(), false);
        Car updatedCar = car.updateCar(carRequestDto);
        carRepository.saveAndFlush(updatedCar);
    }

    @Transactional
    public void deleteCar(CarId carId) {
        if (carRepository.existsByCarId(carId)) {
            Car car = carRepository.findByCarIdAndIsDeletedOrThrow(carId, false);
            carRepository.delete(car);
        }
    }
}
