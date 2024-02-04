package com.fullcar.carpool.domain.service;

import com.fullcar.carpool.domain.form.FormId;
import com.fullcar.core.id.SnowFlake;
import org.springframework.stereotype.Service;

@Service
public class FormIdService {
    private final SnowFlake snowFlake;

    public FormIdService() {
        snowFlake = new SnowFlake(255);
    }
    public FormId nextId() {
        return new FormId(snowFlake.nextId());
    }
}
