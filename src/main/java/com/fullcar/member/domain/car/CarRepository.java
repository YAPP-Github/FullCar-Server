package com.fullcar.member.domain.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, CarId> {
    Optional<Car> findByCarIdAndIsDeleted(CarId carId, boolean isDeleted);
}
