package com.fullcar.carpool.infra.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationDto {
    private String nickName;

    private String deviceToken;

    private String title;

    private String body;
}
