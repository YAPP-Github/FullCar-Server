package com.fullcar.carpool.application.form;

import com.fullcar.carpool.domain.carpool.event.CarpoolDeletedEvent;
import com.fullcar.carpool.domain.form.Form;
import com.fullcar.carpool.domain.form.FormRepository;
import com.fullcar.carpool.domain.form.FormState;
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
public class CarpoolDeletedEventHandler {
    private final FormRepository formRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void rejectForms(CarpoolDeletedEvent carpoolDeletedEvent) {
        log.info("CarpoolDeletedEvent received");
        List<Form> forms = carpoolDeletedEvent.getForms();

        for (Form form: forms) {
            if (form.getFormState() == FormState.REQUEST || form.getFormState() == FormState.ACCEPT) {
                form.reject();
            }
            form.disconnectCarpool();
        }
        formRepository.saveAllAndFlush(forms);
    }
}
