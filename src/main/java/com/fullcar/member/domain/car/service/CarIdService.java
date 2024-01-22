package com.fullcar.member.domain.car.service;

import com.fullcar.member.domain.car.CarId;
import com.fullcar.core.id.SnowFlake;
import org.springframework.stereotype.Service;

@Service
public class CarIdService {
    private final SnowFlake snowFlake;

    public CarIdService() {
        snowFlake = new SnowFlake(255);
    }

    public CarId nextId() {
        return new CarId(snowFlake.nextId());
    }
}
