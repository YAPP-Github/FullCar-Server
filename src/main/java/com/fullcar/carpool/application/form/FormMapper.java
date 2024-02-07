package com.fullcar.carpool.application.form;

import com.fullcar.carpool.domain.carpool.CarpoolId;
import com.fullcar.carpool.domain.form.Cost;
import com.fullcar.carpool.domain.form.Form;
import com.fullcar.carpool.domain.form.Passenger;
import com.fullcar.carpool.domain.service.FormIdService;
import com.fullcar.carpool.presentation.form.dto.request.FormRequestDto;
import com.fullcar.carpool.presentation.form.dto.response.FormResponseDto;
import com.fullcar.member.domain.member.Member;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class FormMapper {
    private final FormIdService formIdService;

    public FormResponseDto toDto(Form form, Member member) {
        return FormResponseDto.builder()
                .id(form.getFormId().getId())
                .pickupLocation(form.getPickupLocation())
                .periodType(form.getCost().getPeriodType())
                .money(form.getCost().getMoney())
                .formState(form.getFormState())
                .companyName(member.getCompany().getCompanyName())
                .nickname(member.getNickname())
                .createdAt(form.getCreatedAt())
                .build();
    }

    public FormResponseDto.FormDetailDto toDetailDto(Form form, Member member) {
        return FormResponseDto.FormDetailDto.builder()
                .id(form.getFormId().getId())
                .pickupLocation(form.getPickupLocation())
                .periodType(form.getCost().getPeriodType())
                .money(form.getCost().getMoney())
                .formState(form.getFormState())
                .companyName(member.getCompany().getCompanyName())
                .nickname(member.getNickname())
                .createdAt(form.getCreatedAt())
                .content(form.getContent())
                .resultMessage(form.getResultMessage())
                .carpoolId(form.getCarpoolId().getId())
                .build();
    }

    public Form toEntity(Member member, CarpoolId carpoolId, FormRequestDto formRequestDto) {
        return Form.builder()
                .formId(formIdService.nextId())
                .content(formRequestDto.getContent())
                .pickupLocation(formRequestDto.getPickupLocation())
                .cost(
                        Cost.builder()
                                .periodType(formRequestDto.getPeriodType())
                                .money(formRequestDto.getMoney())
                                .build()
                )
                .passenger(
                        Passenger.builder()
                                .memberId(member.getId())
                                .build()
                )
                .carpoolId(carpoolId)
                .build();
    }
}
