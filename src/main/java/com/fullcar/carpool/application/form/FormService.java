package com.fullcar.carpool.application.form;

import com.fullcar.carpool.domain.carpool.Carpool;
import com.fullcar.carpool.domain.carpool.CarpoolId;
import com.fullcar.carpool.domain.carpool.CarpoolRepository;
import com.fullcar.carpool.domain.carpool.CarpoolState;
import com.fullcar.carpool.domain.form.*;
import com.fullcar.carpool.domain.form.event.FormStateChangedEvent;
import com.fullcar.carpool.domain.service.NotificationService;
import com.fullcar.carpool.infra.dto.NotificationDto;
import com.fullcar.carpool.presentation.form.dto.request.FormRequestDto;
import com.fullcar.carpool.presentation.form.dto.request.FormUpdateDto;
import com.fullcar.carpool.presentation.form.dto.response.FormResponseDto;
import com.fullcar.core.exception.CustomException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.domain.member.MemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Validated
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class FormService {
    private final FormRepository formRepository;
    private  final CarpoolRepository carpoolRepository;
    private final MemberRepository memberRepository; //TODO: Event 기반으로 변경 필요.
    private final FormMapper formMapper;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public FormResponseDto requestForm(Member member, CarpoolId carpoolId, FormRequestDto formRequestDto) {
        Carpool carpool = carpoolRepository.findByCarpoolIdAndIsDeletedOrThrow(carpoolId, false);
        Member driver = memberRepository.findByIdAndIsDeletedOrThrow(carpool.getDriver().getMemberId(), false);

        if (carpool.getCarpoolState() == CarpoolState.CLOSE) {
            throw new CustomException(ErrorCode.CANNOT_SEND_TO_CLOSED_CARPOOL);
        }

        if (carpool.isMyCarpool(member.getId())) {
            throw new CustomException(ErrorCode.CANNOT_SEND_TO_OWN_CARPOOL);
        }

        Optional<Form> duplicatedForm = formRepository.findByPassengerAndCarpoolIdAndIsDeleted(
                Passenger.builder()
                        .memberId(member.getId())
                        .build(),
                carpoolId,
                false
        );

        if (duplicatedForm.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATED_FORM);
        }

        Form form = formMapper.toEntity(member, carpoolId, formRequestDto);

        eventPublisher.publishEvent(
                new FormStateChangedEvent(
                        NotificationDto.builder()
                                .nickName(driver.getNickname())
                                .deviceToken(driver.getDeviceToken())
                                .title(FormMessage.REQUEST_TITLE.getMessage())
                                .body(FormMessage.REQUEST_BODY.getMessage())
                                .build()
                )
        );

        return formMapper.toDto(
                formRepository.saveAndFlush(form),
                member
        );
    }

    @Transactional(readOnly = true)
    public List<FormResponseDto> readSentFormList(Member member) {
        return formRepository.findAllByPassengerAndIsDeletedOrderByCreatedAtDesc(
                Passenger.builder()
                        .memberId(member.getId())
                        .build(),
                false
        ).stream()
                .map(form -> formMapper.toDto(form, member))
                .toList();
    }

    @Transactional(readOnly = true)
    public FormResponseDto.FormDetailDto readForm(FormId formId) {
        Form form = formRepository.findByFormIdAndIsDeletedOrThrow(formId, false);
        Member member = memberRepository.findByIdAndIsDeletedOrThrow(form.getPassenger().getMemberId(), false);

        return formMapper.toDetailDto(form, member);
    }

    @Transactional(readOnly = true)
    public List<FormResponseDto> readReceivedFormList(Member member) {
        return formRepository.findReceivedForm(member.getId().getId())
                .stream()
                .map(form -> formMapper.toDto(
                        form,
                        memberRepository.findByIdAndIsDeletedOrThrow(form.getPassenger().getMemberId(), false)) // TODO: N+1 문제 해결 필요.
                )
                .toList();
    }

    @Transactional
    public FormResponseDto.FormDetailDto updateForm(Member member, FormId formId, FormUpdateDto formUpdateDto) {
        Form form = formRepository.findByFormIdAndIsDeletedOrThrow(formId, false);
        Member passenger = memberRepository.findByIdAndIsDeletedOrThrow(form.getPassenger().getMemberId(), false);
        Carpool carpool = carpoolRepository.findByCarpoolIdAndIsDeletedOrThrow(form.getCarpoolId(), false);

        if (!carpool.isMyCarpool(member.getId())) {
            throw new CustomException(ErrorCode.CANNOT_CHANGE_FORM_STATE);
        }

        form.changeFormState(formUpdateDto, passenger);

        return formMapper.toDetailDto(
                formRepository.saveAndFlush(form),
                memberRepository.findByIdAndIsDeletedOrThrow(
                        form.getPassenger().getMemberId(),
                        false
                )
        );
    }
}
