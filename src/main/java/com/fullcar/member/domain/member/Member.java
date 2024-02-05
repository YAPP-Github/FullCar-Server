package com.fullcar.member.domain.member;

import com.fullcar.member.domain.car.CarId;
import com.fullcar.member.domain.auth.SocialId;
import jakarta.persistence.*;
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

    @AttributeOverrides(
            @AttributeOverride(name = "id", column = @Column(name = "car_id"))
    )
    private CarId carId;

    private SocialId socialId;

    @Embedded
    private Company company;

    @Length(min=2, max=10)
    @Column(length = 10)
    private String nickname;

    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Builder.Default
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

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

    public void updateCarInformation(CarId carId) {
        this.carId = carId;
    }

    public void saveOnBoardingInfo(Member member) {
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.gender = member.getGender();
        this.company = new Company(member.getCompany().getCompanyName(), member.getCompany().getLatitude(), member.getCompany().getLongitude());
    }

    public void clearRefreshTokenAndDeviceToken() {
        this.deviceToken = null;
        this.refreshToken = null;
    }
}
