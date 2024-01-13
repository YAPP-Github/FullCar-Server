package com.fullcar.member.application;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "apple")
@Getter
public class AppleProperties {
    private String teamId;
    private String keyId;
    private String clientId;
    private String audience;
    private String iss;
}
