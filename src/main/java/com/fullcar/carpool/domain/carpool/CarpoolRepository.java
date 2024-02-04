package com.fullcar.carpool.domain.carpool;

import com.fullcar.core.exception.CustomException;
import com.fullcar.core.response.ErrorCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarpoolRepository extends JpaRepository<Carpool, CarpoolId> {
    Optional<Carpool> findByCarpoolIdAndIsDeleted(CarpoolId carpoolId, boolean isDeleted);
    Slice<Carpool> findAllByIsDeletedOrderByCreatedAtDesc(boolean isDeleted, Pageable pageable);

    default Carpool findByCarpoolIdAndIsDeletedOrThrow(CarpoolId carpoolId, boolean isDeleted) {
        return findByCarpoolIdAndIsDeleted(carpoolId, isDeleted).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_CARPOOL)
        );
    }
}
