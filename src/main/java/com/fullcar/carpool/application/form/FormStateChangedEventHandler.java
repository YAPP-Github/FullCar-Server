package com.fullcar.carpool.application.form;

import com.fullcar.carpool.domain.form.event.FormStateChangedEvent;
import com.fullcar.carpool.domain.service.NotificationService;
import com.fullcar.carpool.infra.dto.NotificationDto;
import com.fullcar.member.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormStateChangedEventHandler {
    private final NotificationService notificationService;
    private final MemberRepository memberRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNotification(FormStateChangedEvent formStateChangedEvent) {
        notificationService.sendNotification(
                NotificationDto.builder()
                .member(
                        memberRepository.findByIdAndIsDeletedOrThrow(formStateChangedEvent.getMemberId(), false)
                )
                .title(formStateChangedEvent.getTitle())
                .body(formStateChangedEvent.getBody())
                .build()
        );
        log.info("Notification send");
    }
}
