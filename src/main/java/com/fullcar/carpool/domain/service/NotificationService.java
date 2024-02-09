package com.fullcar.carpool.domain.service;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface NotificationService {
    void sendNotification(String nickname, String deviceToken, String title, String body);
}
