package com.fullcar.carpool.application;

import com.fullcar.carpool.domain.Carpool;
import com.fullcar.carpool.domain.Cost;
import com.fullcar.carpool.domain.Driver;
import com.fullcar.carpool.domain.service.CarpoolIdService;
import com.fullcar.carpool.presentation.dto.CarpoolDto;
import com.fullcar.member.domain.MemberId;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CarpoolMapper {
    private final CarpoolIdService carpoolIdService;

    public CarpoolDto toDto(Carpool carpool) {
        return CarpoolDto.builder()
                .id(carpool.getCarpoolId().getId())
                .pickupLocation(carpool.getPickupLocation())
                .periodType(carpool.getCost().getPeriodType())
                .money(carpool.getCost().getMoney())
                .content(carpool.getContent())
                .moodType(carpool.getMoodType())
                .build();
    }

    public Carpool toEntity(MemberId memberId, CarpoolDto carpoolDto) {
        return Carpool.builder()
                .carpoolId(carpoolIdService.nextId())
                .pickupLocation(carpoolDto.getPickupLocation())
                .cost(
                        Cost.builder()
                                .periodType(carpoolDto.getPeriodType())
                                .money(carpoolDto.getMoney())
                                .build()
                )
                .content(carpoolDto.getContent())
                .moodType(carpoolDto.getMoodType())
                .driver(
                        Driver.builder()
                                .memberId(memberId)
                                .build()
                )
                .build();
    }
}
