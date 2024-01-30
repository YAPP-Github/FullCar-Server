package com.fullcar.member.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;

import static lombok.AccessLevel.PROTECTED;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Company {
    @Column(name = "company_name")
    private String companyName;

    private BigDecimal latitude;

    private BigDecimal longitude;
}
