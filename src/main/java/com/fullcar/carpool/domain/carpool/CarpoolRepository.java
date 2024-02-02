package com.fullcar.carpool.domain.carpool;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarpoolRepository extends JpaRepository<Carpool, CarpoolId> {
    Optional<Carpool> findByCarpoolIdAndIsDeleted(CarpoolId carpoolId, boolean isDeleted);
    Slice<Carpool> findAllByIsDeletedOrderByCreatedAtDesc(boolean isDeleted, Pageable pageable);
}
