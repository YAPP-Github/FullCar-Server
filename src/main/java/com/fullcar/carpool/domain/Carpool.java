package com.fullcar.carpool.domain;

import com.fullcar.carpool.domain.event.MemberRequestEvent;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "carpool")
public class Carpool {

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

    @Transient
    private final Collection<DomainEvent> domainEvents;

    @DomainEvents
    Collection<Object> domainEvents() {
        List<Object> domainEvents = new ArrayList<>();
        domainEvents.add(
                new MemberRequestEvent(this.driver.getMemberId())
        );
        return domainEvents;
    }

    @AfterDomainEventPublication
    public void clearEvents() {
        domainEvents.clear();
    }
}
