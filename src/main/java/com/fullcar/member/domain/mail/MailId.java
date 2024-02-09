package com.fullcar.member.domain.mail;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;

@Embeddable
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class MailId implements Serializable {

    @Column(name = "id")
    private Long id;
}
