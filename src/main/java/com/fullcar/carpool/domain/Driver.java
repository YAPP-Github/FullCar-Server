package com.fullcar.carpool.domain;

import com.fullcar.member.domain.MemberId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;


@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Driver {
    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "driver_id"))
    )
    private MemberId memberId;

    //:TODO CarId
}
