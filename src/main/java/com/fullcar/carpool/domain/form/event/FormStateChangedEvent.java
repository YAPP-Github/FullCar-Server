package com.fullcar.carpool.domain.form.event;

import com.fullcar.carpool.infra.dto.NotificationDto;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.domain.member.MemberId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormStateChangedEvent {
    private MemberId memberId;

    private String title;

    private String body;
}
