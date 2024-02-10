package com.fullcar.member.domain.mail.service;

import com.fullcar.core.id.SnowFlake;
import com.fullcar.member.domain.mail.MailId;
import org.springframework.stereotype.Service;

@Service
public class MailIdService {
    private final SnowFlake snowFlake;

    public MailIdService() {
        snowFlake = new SnowFlake(255);
    }

    public MailId nextId() {
        return new MailId(snowFlake.nextId());
    }
}
