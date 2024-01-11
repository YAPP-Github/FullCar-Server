package com.fullcar.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class MemberId implements Serializable {

    @Column(name = "id")
    private Long id;

}
