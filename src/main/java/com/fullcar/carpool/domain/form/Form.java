package com.fullcar.carpool.domain.form;

import com.fullcar.carpool.domain.carpool.CarpoolId;
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
}
