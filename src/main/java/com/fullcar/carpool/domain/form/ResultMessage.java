package com.fullcar.carpool.domain.form;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ResultMessage {
    private String contact;

    @Column(name = "to_passenger")
    private String toPassenger;
}
