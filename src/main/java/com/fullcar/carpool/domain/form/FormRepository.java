package com.fullcar.carpool.domain.form;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormRepository extends JpaRepository<Form, FormId> {
    List<Form> findAllByPassengerAndIsDeletedOrderByCreatedAtDesc(Passenger passenger, boolean isDeleted);
}
