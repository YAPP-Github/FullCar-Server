package com.fullcar.member.application.car;

import com.fullcar.member.domain.car.Car;
import com.fullcar.member.domain.car.CarRepository;
import com.fullcar.member.presentation.car.dto.CarDto;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.domain.member.MemberRepository;
import com.fullcar.member.presentation.car.dto.request.CarUpdateRequestDto;
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
    public CarDto registerCar(Member member, CarDto carDto) {
        Car car = carMapper.toEntity(carDto);

        // 존재하는 유저인지
        memberRepository.findByIdAndIsDeletedOrThrow(member.getId(), false);
        // 해당 유저에 차량이 등록되어 있는지
        memberRepository.existsByCarIdOrThrow(car.getCarId());
        member.updateCarInformation(car.getCarId());

        return carMapper.toDto(
                carRepository.saveAndFlush(car)
        );
    }

    @Transactional(readOnly = true)
    public CarDto getCar(Member member) {
        Car car = carRepository.findByCarIdAndIsDeletedOrThrow(member.getCarId(), false);
        return carMapper.toDto(car);
    }

    @Transactional
    public void updateCar(Member member, CarUpdateRequestDto carUpdateRequestDto) {
        Car car = carRepository.findByCarIdAndIsDeletedOrThrow(member.getCarId(), false);
        Car updatedCar = car.updateCar(carUpdateRequestDto);
        carRepository.saveAndFlush(updatedCar);
    }
}
