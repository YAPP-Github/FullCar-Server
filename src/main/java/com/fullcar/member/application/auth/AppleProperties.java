package com.fullcar.member.application.auth;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppleProperties {
    private String teamId;
    private String keyId;
    private String clientId;
    private String audience;
    private String iss;
}
