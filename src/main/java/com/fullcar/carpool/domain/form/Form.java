package com.fullcar.carpool.domain.form;

import com.fullcar.carpool.domain.carpool.CarpoolId;
import com.fullcar.carpool.domain.form.event.FormStateChangedEvent;
import com.fullcar.carpool.infra.dto.NotificationDto;
import com.fullcar.carpool.presentation.form.dto.request.FormUpdateDto;
import com.fullcar.core.exception.CustomException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "form")
public class Form extends AbstractAggregateRoot<Form> {

    @EmbeddedId
    private FormId formId;

    private String content;

    @Column(name = "pickup_location")
    private String pickupLocation;

    @Embedded
    private Cost cost;

    @Enumerated(EnumType.STRING)
    @Column(name = "form_state")
    @Builder.Default
    private FormState formState = FormState.REQUEST;

    @Embedded
    private ResultMessage resultMessage;

    @Embedded
    private Passenger passenger;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "carpool_id"))
    private CarpoolId carpoolId;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void changeFormState(FormUpdateDto formUpdateDto) {
        FormState formState = formUpdateDto.getFormState();

        if (formState == FormState.ACCEPT) {
            this.accept(formUpdateDto.getContact(), formUpdateDto.getToPassenger());
        }
        else if (formState == FormState.REJECT) {
            this.reject();
        }
        else {
            throw new CustomException(ErrorCode.INVALID_FORM_STATE);
        }
    }

    public void accept(String contact, String toPassenger) {
        this.formState = FormState.ACCEPT;
        this.resultMessage = ResultMessage.builder()
                .contact(contact)
                .toPassenger(toPassenger)
                .build();

        registerEvent(
                FormStateChangedEvent.builder()
                        .memberId(this.passenger.getMemberId())
                        .title(FormMessage.ACCEPT_TITLE.getMessage())
                        .body(FormMessage.ACCEPT_BODY.getMessage())
                        .build()
        );
    }

    public void reject() {
        this.formState = FormState.REJECT;
        this.resultMessage = ResultMessage.builder()
                .toPassenger(FormMessage.REJECT_MESSAGE.getMessage())
                .build();

        registerEvent(
                FormStateChangedEvent.builder()
                        .memberId(this.passenger.getMemberId())
                        .title(FormMessage.REJECT_TITLE.getMessage())
                        .body(FormMessage.REJECT_BODY.getMessage())
                        .build()
        );
    }

    public void disconnectCarpool() {
        this.carpoolId = null;
    }
}
