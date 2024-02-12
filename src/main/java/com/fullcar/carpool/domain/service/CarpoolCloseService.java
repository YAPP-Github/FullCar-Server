package com.fullcar.carpool.domain.service;

import com.fullcar.carpool.domain.carpool.Carpool;
import com.fullcar.carpool.domain.form.Form;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CarpoolCloseService {

    public Carpool closeCarpool(Carpool carpool) {
        carpool.close();

        return carpool;
    }

    public List<Form> rejectForms(List<Form> forms) {
        for (Form form : forms) {
            form.reject();
        }

        return forms;
    }
}
