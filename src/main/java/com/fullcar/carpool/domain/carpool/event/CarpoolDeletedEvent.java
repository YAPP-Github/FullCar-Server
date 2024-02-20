package com.fullcar.carpool.domain.carpool.event;

import com.fullcar.carpool.domain.form.Form;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CarpoolDeletedEvent {
    private List<Form> forms;
}
