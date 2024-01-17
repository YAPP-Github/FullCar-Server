package com.fullcar.carpool.application;

import com.fullcar.carpool.domain.Carpool;
import com.fullcar.carpool.domain.CarpoolRepository;
import com.fullcar.carpool.presentation.dto.CarpoolDto;
import com.fullcar.member.domain.MemberId;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CarpoolService {
    private final CarpoolRepository carpoolRepository;
    private final CarpoolMapper carpoolMapper;

    @Transactional
    public CarpoolDto registerCarpool(MemberId memberId, CarpoolDto carpoolDto) {
        Carpool carpool = carpoolMapper.toEntity(memberId, carpoolDto);

        return carpoolMapper.toDto(
                carpoolRepository.saveAndFlush(carpool)
        );
    }
}
