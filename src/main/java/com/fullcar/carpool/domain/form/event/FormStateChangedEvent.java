package com.fullcar.carpool.domain.form.event;

import com.fullcar.carpool.infra.dto.NotificationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FormStateChangedEvent {
    private NotificationDto notificationDto;
}
