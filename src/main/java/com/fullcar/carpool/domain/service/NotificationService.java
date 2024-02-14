package com.fullcar.carpool.domain.service;

import com.fullcar.carpool.infra.dto.NotificationDto;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface NotificationService {
    void sendNotification(NotificationDto notificationDto);
}
