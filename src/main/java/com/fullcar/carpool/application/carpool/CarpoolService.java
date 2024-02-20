package com.fullcar.carpool.application.carpool;

import com.fullcar.carpool.domain.carpool.Carpool;
import com.fullcar.carpool.domain.carpool.CarpoolId;
import com.fullcar.carpool.domain.carpool.CarpoolRepository;
import com.fullcar.carpool.domain.carpool.Driver;
import com.fullcar.carpool.domain.form.Form;
import com.fullcar.carpool.domain.form.FormRepository;
import com.fullcar.carpool.domain.form.FormState;
import com.fullcar.carpool.presentation.carpool.dto.request.CarpoolRequestDto;
import com.fullcar.carpool.presentation.carpool.dto.response.CarpoolResponseDto;
import com.fullcar.carpool.presentation.carpool.dto.response.MyCarpoolDto;
import com.fullcar.core.exception.CustomException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.domain.car.Car;
import com.fullcar.member.domain.car.CarRepository;
import com.fullcar.member.domain.member.Member;

import com.fullcar.member.domain.member.MemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;


@Validated
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CarpoolService {
    private final CarpoolRepository carpoolRepository;
    private final CarRepository carRepository;  //:TODO Event 기반으로 변경
    private final MemberRepository memberRepository; //:TODO Event 기반으로 변경
    private final FormRepository formRepository;
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

    @Transactional(readOnly = true)
    public CarpoolResponseDto.CarpoolDetailDtO getCarpool(Member member, CarpoolId carpoolId) {
        Carpool carpool = carpoolRepository.findByCarpoolIdAndIsDeleted(carpoolId, false)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_CARPOOL));

        Car car = carRepository.findByCarIdAndIsDeleted(member.getCarId(), false).
                orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_CAR));

        return carpoolMapper.toDetailDto(carpool, member, car);
    }

    @Transactional(readOnly = true)
    public List<MyCarpoolDto> getMyCarpoolList(Member member) {
        List<Carpool> carpools = carpoolRepository.findAllByDriverAndIsDeletedOrderByCreatedAtDesc(
                Driver.builder()
                .memberId(member.getId())
                .build(),
                false
        );

        return carpools.stream()
                .map(carpool -> carpoolMapper.toMyCarpoolDto(carpool, member))
                .toList();
    }

    @Transactional
    public CarpoolResponseDto.CarpoolDetailDtO closeCarpool(Member member, CarpoolId carpoolId) {
        Carpool carpool = carpoolRepository.findByCarpoolIdAndIsDeletedOrThrow(carpoolId, false);
        Car car = carRepository.findByCarIdAndIsDeletedOrThrow(member.getCarId(), false);

        if (!carpool.isMyCarpool(member.getId())) {
            throw new CustomException(ErrorCode.CANNOT_CLOSE_CARPOOL);
        }
        List<Form> forms = formRepository.findAllByCarpoolIdAndIsDeleted(carpoolId, false);

        carpool.close(forms);

        carpoolRepository.saveAndFlush(carpool);

        return carpoolMapper.toDetailDto(carpool, member, car);
    }

    @Transactional
    public CarpoolResponseDto deleteCarpool(Member member, CarpoolId carpoolId) {
        Carpool carpool = carpoolRepository.findByCarpoolIdAndIsDeletedOrThrow(carpoolId, false);

        if (!carpool.isMyCarpool(member.getId())) {
            throw new CustomException(ErrorCode.CANNOT_DELETE_CARPOOL);
        }
        List<Form> forms = formRepository.findAllByCarpoolIdAndIsDeleted(carpoolId, false);

        carpool.delete(forms);

        carpoolRepository.saveAndFlush(carpool);

        return carpoolMapper.toDto(carpool, member);
    }
}
