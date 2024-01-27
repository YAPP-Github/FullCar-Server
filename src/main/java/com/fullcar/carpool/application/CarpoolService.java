package com.fullcar.carpool.application;

import com.fullcar.carpool.domain.Carpool;
import com.fullcar.carpool.domain.CarpoolRepository;
import com.fullcar.carpool.presentation.dto.request.CarpoolRequestDto;
import com.fullcar.carpool.presentation.dto.response.CarpoolResponseDto;
import com.fullcar.member.domain.member.Member;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;


@Validated
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CarpoolService {
    private final CarpoolRepository carpoolRepository;
    private final CarpoolMapper carpoolMapper;

    @Transactional
    public CarpoolResponseDto registerCarpool(Member member, CarpoolRequestDto carpoolRequestDto) {
        Carpool carpool = carpoolMapper.toEntity(member, carpoolRequestDto);

        return carpoolMapper.toDto(
                carpoolRepository.saveAndFlush(carpool),
                member
        );
    }

    @Transactional(readOnly = true)
    public Slice<CarpoolResponseDto> getCarpoolList(Member member, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Slice<Carpool> carpools = carpoolRepository.findAllByIsDeletedOrderByCreatedAtDesc(false, pageRequest);

        List<CarpoolResponseDto> carpoolResponseDtos = carpools.getContent().stream()
                .map(carpool -> carpoolMapper.toDto(carpool, member))
                .toList();

        return new SliceImpl<>(carpoolResponseDtos, carpools.getPageable(), carpools.hasNext());
    }
}
