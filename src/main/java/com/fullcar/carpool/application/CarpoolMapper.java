package com.fullcar.carpool.application;

import com.fullcar.carpool.domain.Carpool;
import com.fullcar.carpool.domain.Cost;
import com.fullcar.carpool.domain.Driver;
import com.fullcar.carpool.domain.service.CarpoolIdService;
import com.fullcar.carpool.presentation.dto.request.CarpoolRequestDto;
import com.fullcar.carpool.presentation.dto.response.CarpoolResponseDto;
import com.fullcar.member.domain.car.Car;
import com.fullcar.member.domain.member.Member;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CarpoolMapper {
    private final CarpoolIdService carpoolIdService;

    public CarpoolResponseDto toDto(Carpool carpool, Member member) {
        return CarpoolResponseDto.builder()
                .id(carpool.getCarpoolId().getId())
                .pickupLocation(carpool.getPickupLocation())
                .periodType(carpool.getCost().getPeriodType())
                .money(carpool.getCost().getMoney())
                .content(carpool.getContent())
                .moodType(carpool.getMoodType())
                .gender(member.getGender())
                .createdAt(carpool.getCreatedAt())
                .build();
    }

    public CarpoolResponseDto.CarpoolDetailDTO toDetailDto(Carpool carpool, Member member, Car car) {
        return CarpoolResponseDto.CarpoolDetailDTO.builder()
                .id(carpool.getCarpoolId().getId())
                .pickupLocation(carpool.getPickupLocation())
                .periodType(carpool.getCost().getPeriodType())
                .money(carpool.getCost().getMoney())
                .content(carpool.getContent())
                .moodType(carpool.getMoodType())
                .carpoolState(carpool.getCarpoolState())
                .companyName(member.getCompany())
                .gender(member.getGender())
                .createdAt(carpool.getCreatedAt())
                .nickname(member.getNickname())
                .carNo(car.getCarNo())
                .carName(car.getCarName())
                .carBrand(car.getCarBrand())
                .carColor(car.getCarColor())
                .build();
    }

    public Carpool toEntity(Member member, CarpoolRequestDto carpoolRequestDto) {
        return Carpool.builder()
                .carpoolId(carpoolIdService.nextId())
                .pickupLocation(carpoolRequestDto.getPickupLocation())
                .cost(
                        Cost.builder()
                                .periodType(carpoolRequestDto.getPeriodType())
                                .money(carpoolRequestDto.getMoney())
                                .build()
                )
                .content(carpoolRequestDto.getContent())
                .moodType(carpoolRequestDto.getMoodType())
                .driver(
                        Driver.builder()
                                .memberId(member.getId())
                                .build()
                )
                .build();
    }
}
