package com.fullcar.member.application.auth;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoProperties {
    private String adminKey;
}
