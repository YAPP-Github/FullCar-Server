package com.fullcar.carpool.domain.form;

import com.fullcar.carpool.domain.carpool.PeriodType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Cost {
    private Long money;

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type")
    private PeriodType periodType;
}
