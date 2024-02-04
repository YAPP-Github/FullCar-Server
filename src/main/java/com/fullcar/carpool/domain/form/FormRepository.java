package com.fullcar.carpool.domain.form;

import com.fullcar.carpool.domain.carpool.CarpoolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormRepository extends JpaRepository<Form, FormId> {
    List<Form> findAllByPassengerAndIsDeletedOrderByCreatedAtDesc(Passenger passenger, boolean isDeleted);
    Optional<Form> findByPassengerAndCarpoolIdAndIsDeleted(Passenger passenger, CarpoolId carpoolId, boolean isDeleted);
}
