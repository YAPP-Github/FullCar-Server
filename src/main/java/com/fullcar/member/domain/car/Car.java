package com.fullcar.member.domain.car;

import com.fullcar.member.presentation.car.dto.request.CarRequestDto;
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

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Car {

    @EmbeddedId
    private CarId carId;

    @Column(name = "car_no", nullable = false)
    private String carNo;

    @Column(name = "car_name", nullable = false)
    private String carName;

    @Column(name = "car_brand", nullable = false)
    private String carBrand;

    @Column(name = "car_color", nullable = false)
    private String carColor;

    @Builder.Default
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Car updateCar(CarRequestDto carUpdateRequestDto) {
        this.carNo = carUpdateRequestDto.getCarNo();
        this.carName = carUpdateRequestDto.getCarName();
        this.carBrand = carUpdateRequestDto.getCarBrand();
        this.carColor = carUpdateRequestDto.getCarColor();
        return this;
    }
}
