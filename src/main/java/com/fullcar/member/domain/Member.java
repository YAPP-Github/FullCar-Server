package com.fullcar.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Member {

    @EmbeddedId
    private MemberId id;

    private String socialId;

    private String company;

    @Length(min=2, max=10)
    @Column(length = 10)
    private String nickname;

    private String email;

    private String gender;

    private String ageRange;

    private boolean flag;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "device_token", nullable = false)
    private String deviceToken;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void loginMember(String deviceToken, String refreshToken) {
        this.deviceToken = deviceToken;
        this.refreshToken = refreshToken;
    }
}
