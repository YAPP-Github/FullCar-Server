package com.fullcar.member.application.car;

import com.fullcar.member.domain.car.Car;
import com.fullcar.member.domain.car.CarRepository;
import com.fullcar.member.presentation.car.dto.CarDto;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.domain.member.MemberId;
import com.fullcar.member.domain.member.MemberRepository;
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
    public CarDto registerCar(MemberId memberId, CarDto carDto) {
        Car car = carMapper.toEntity(carDto);

        Member member = memberRepository.findByIdAndIsDeletedOrThrow(memberId, false);
        member.updateCarInformation(car.getCarId());

        return carMapper.toDto(
                carRepository.saveAndFlush(car)
        );
    }
}
