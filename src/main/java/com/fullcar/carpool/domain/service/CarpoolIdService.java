package com.fullcar.carpool.domain.service;

import com.fullcar.carpool.domain.CarpoolId;
import com.fullcar.core.id.SnowFlake;
import org.springframework.stereotype.Service;

@Service
public class CarpoolIdService {
    private final SnowFlake snowFlake;

    public CarpoolIdService() {
        snowFlake = new SnowFlake(255);
    }
    public CarpoolId nextId() {
        return new CarpoolId(snowFlake.nextId());
    }
}
