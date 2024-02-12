package com.fullcar.member.domain.car;

import com.fullcar.core.exception.CustomException;
import com.fullcar.core.response.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, CarId> {
    Optional<Car> findByCarIdAndIsDeleted(CarId carId, boolean isDeleted);

    boolean existsByCarId(CarId carId);

    default Car findByCarIdAndIsDeletedOrThrow(CarId carId, boolean isDeleted) {
        return findByCarIdAndIsDeleted(carId, isDeleted)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_CAR));
    }
}
