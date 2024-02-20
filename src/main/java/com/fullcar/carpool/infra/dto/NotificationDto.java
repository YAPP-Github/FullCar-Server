package com.fullcar.carpool.infra.dto;

import com.fullcar.member.domain.member.Member;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationDto {
    private Member member;

    private String title;

    private String body;
}
