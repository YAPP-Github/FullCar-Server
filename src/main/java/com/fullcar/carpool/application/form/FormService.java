package com.fullcar.carpool.application.form;

import com.fullcar.carpool.domain.carpool.Carpool;
import com.fullcar.carpool.domain.carpool.CarpoolId;
import com.fullcar.carpool.domain.carpool.CarpoolRepository;
import com.fullcar.carpool.domain.form.Form;
import com.fullcar.carpool.domain.form.FormRepository;
import com.fullcar.carpool.domain.form.Passenger;
import com.fullcar.carpool.presentation.form.dto.request.FormRequestDto;
import com.fullcar.carpool.presentation.form.dto.response.FormResponseDto;
import com.fullcar.core.exception.CustomException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.domain.member.MemberId;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class FormService {
    private final FormRepository formRepository;
    private  final CarpoolRepository carpoolRepository;
    private final FormMapper formMapper;

    @Transactional
    public FormResponseDto requestForm(Member member, CarpoolId carpoolId, FormRequestDto formRequestDto) {
        Carpool carpool = carpoolRepository.findByCarpoolIdAndIsDeletedOrThrow(carpoolId, false);

        Form form = formMapper.toEntity(member, carpoolId, formRequestDto);

        return formMapper.toDto(
                formRepository.saveAndFlush(form),
                member
        );
    }

    @Transactional(readOnly = true)
    public List<FormResponseDto> getSentForm(Member member) {
        return formRepository.findAllByPassengerAndIsDeletedOrderByCreatedAtDesc(
                Passenger.builder()
                        .memberId(member.getId())
                        .build(),
                false
        ).stream()
                .map(form -> formMapper.toDto(form, member))
                .collect(Collectors.toList());
    }

}
