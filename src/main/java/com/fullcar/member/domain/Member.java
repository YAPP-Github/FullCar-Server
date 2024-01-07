package com.fullcar.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @EmbeddedId
    private MemberId id;

    private long clientId;

    private String company;

    private String nickname;

    private String email;

    private String gender;

    private String ageRange;

    private boolean flag;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Member(MemberId id, long clientId, String company, String nickname, String email, String gender, String ageRange, boolean flag, boolean isDeleted, String refreshToken, String fcmToken, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.clientId = clientId;
        this.company = company;
        this.nickname = nickname;
        this.email = email;
        this.gender = gender;
        this.ageRange = ageRange;
        this.flag = flag;
        this.isDeleted = isDeleted;
        this.refreshToken = refreshToken;
        this.fcmToken = fcmToken;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
