package com.fullcar.carpool.application.Alarm;

import com.fullcar.core.exception.CustomException;
import com.fullcar.core.response.ErrorCode;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmService {

    private final FirebaseMessaging firebaseMessaging;

    public void sendNotification(String nickname, String deviceToken, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(nickname + "님! " + title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(deviceToken)
                .setNotification(notification)
                .build();

        try {
            firebaseMessaging.send(message);
        }
        catch (FirebaseMessagingException e){
            System.out.println(e);
            throw new CustomException(ErrorCode.FAILED_TO_SEND_NOTIFICATION);
        }
    }
}
