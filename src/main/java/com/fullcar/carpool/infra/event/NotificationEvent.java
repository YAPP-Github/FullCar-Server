package com.fullcar.carpool.infra.event;

import com.fullcar.carpool.infra.dto.NotificationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationEvent {
    private NotificationDto notificationDto;
}
