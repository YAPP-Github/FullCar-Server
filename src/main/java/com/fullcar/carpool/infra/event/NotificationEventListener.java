package com.fullcar.carpool.infra.event;

import com.fullcar.carpool.domain.form.event.FormStateChangedEvent;
import com.fullcar.carpool.domain.service.NotificationService;
import com.fullcar.carpool.infra.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableAsync
public class NotificationEventListener {
    private final NotificationService notificationService;

    @Async
    @EventListener
    public void sendNotification(FormStateChangedEvent formStateChangedEvent) {
        notificationService.sendNotification(formStateChangedEvent.getNotificationDto());
        log.info("Notification send");
    }
}
