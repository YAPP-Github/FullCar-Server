package com.fullcar.carpool.domain.form;

import com.fullcar.member.domain.member.MemberId;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Passenger {
    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "passenger_id"))
    private MemberId memberId;
}
