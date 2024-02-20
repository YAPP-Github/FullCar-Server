package com.fullcar.carpool.domain.carpool;

import com.fullcar.carpool.domain.carpool.event.CarpoolClosedEvent;
import com.fullcar.carpool.domain.form.Form;
import com.fullcar.carpool.domain.form.FormState;
import com.fullcar.core.exception.CustomException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.domain.member.MemberId;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "carpool")
public class Carpool extends AbstractAggregateRoot<Carpool> {

    @EmbeddedId
    private CarpoolId carpoolId;

    private String content;

    @Column(name = "pickup_location")
    private String pickupLocation;

    @Embedded
    private Driver driver;

    @Embedded
    private Cost cost;

    @Enumerated(EnumType.STRING)
    @Column(name = "carpool_state")
    @Builder.Default
    private CarpoolState carpoolState = CarpoolState.OPEN;

    @Enumerated(EnumType.STRING)
    @Column(name = "mood_type")
    private MoodType moodType;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private boolean isDeleted = false;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public boolean isMyCarpool(MemberId memberId) {
        return this.getDriver().getMemberId().getId().equals(memberId.getId());
    }

    public void close(List<Form> forms) {
        if (this.carpoolState == CarpoolState.CLOSE) {
            throw new CustomException(ErrorCode.ALREADY_CLOSED);
        }
        this.carpoolState = CarpoolState.CLOSE;
        registerEvent(
                new CarpoolClosedEvent(
                        forms.stream()
                                .filter(form -> form.getFormState() == FormState.REQUEST)
                                .toList()
                )
        );
    }
}
