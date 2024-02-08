package com.fullcar.carpool.domain.form;

import com.fullcar.carpool.domain.carpool.CarpoolId;
import com.fullcar.carpool.presentation.form.dto.request.FormUpdateDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "form")
public class Form {
    private static final String REJECT_MESSAGE = "카풀 매칭에 실패했어. 다른 카풀을 찾아보세요!";

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

    @Column(name = "result_message")
    private String resultMessage;

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
    }
    public void accept(String contact, String toPassenger) {
        this.formState = FormState.ACCEPT;
        this.resultMessage = contact + "\n" + toPassenger;
    }

    public void reject() {
        this.formState = FormState.REJECT;
        this.resultMessage = REJECT_MESSAGE;
    }
}
