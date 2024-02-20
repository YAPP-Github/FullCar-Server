package com.fullcar.carpool.application.form;


import com.fullcar.carpool.domain.carpool.event.CarpoolClosedEvent;
import com.fullcar.carpool.domain.form.Form;
import com.fullcar.carpool.domain.form.FormRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarpoolClosedEventHandler {
    private final FormRepository formRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void rejectForms(CarpoolClosedEvent carpoolClosedEvent) {
        List<Form> forms = carpoolClosedEvent.getForms();

        forms.forEach(Form::reject);
        formRepository.saveAllAndFlush(forms);
    }
}
