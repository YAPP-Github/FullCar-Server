package com.fullcar.member.domain.member.service;

import com.fullcar.core.id.SnowFlake;
import com.fullcar.member.domain.member.MemberId;
import org.springframework.stereotype.Service;

@Service
public class MemberIdService {
    private final SnowFlake snowFlake;

    public MemberIdService() {
        snowFlake = new SnowFlake(255);
    }
    public MemberId nextId() {
        return new MemberId(snowFlake.nextId());
    }
}
