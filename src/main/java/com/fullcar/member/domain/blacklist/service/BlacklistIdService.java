package com.fullcar.member.domain.blacklist.service;

import com.fullcar.core.id.SnowFlake;
import com.fullcar.member.domain.blacklist.BlacklistId;
import org.springframework.stereotype.Service;

@Service
public class BlacklistIdService {
    private final SnowFlake snowFlake;

    public BlacklistIdService() {
        snowFlake = new SnowFlake(255);
    }

    public BlacklistId nextId() {
        return new BlacklistId(snowFlake.nextId());
    }
}
