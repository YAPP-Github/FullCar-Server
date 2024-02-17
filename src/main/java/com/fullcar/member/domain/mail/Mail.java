package com.fullcar.member.domain.mail;

import com.fullcar.member.domain.member.MemberId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Mail {
    @EmbeddedId
    private MailId mailId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "member_id"))
    private MemberId memberId;

    private Integer code;

    public void updateEmailCode(Integer code) {
        this.code = code;
    }
}
