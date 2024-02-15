package com.fullcar.carpool.domain.form;

import com.fullcar.carpool.domain.carpool.Carpool;
import com.fullcar.carpool.domain.carpool.CarpoolId;
import com.fullcar.core.exception.CustomException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.domain.member.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormRepository extends JpaRepository<Form, FormId> {
    Optional<Form> findByFormIdAndIsDeleted(FormId formId, boolean isDeleted);
    List<Form> findAllByPassengerAndIsDeletedOrderByCreatedAtDesc(Passenger passenger, boolean isDeleted);
    Optional<Form> findByPassengerAndCarpoolIdAndIsDeleted(Passenger passenger, CarpoolId carpoolId, boolean isDeleted);
    @Query("select f from Form f join Carpool c on f.carpoolId.id = c.carpoolId.id where c.driver.memberId.id = :memberId order by f.createdAt desc")
    List<Form> findReceivedForm(
            @Param("memberId") Long memberId
    );

    List<Form> findAllByCarpoolIdAndIsDeleted(CarpoolId carpoolId, boolean isDeleted);

    default Form findByFormIdAndIsDeletedOrThrow(FormId formId, boolean isDeleted) {
        return findByFormIdAndIsDeleted(formId, isDeleted).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_EXIST_FORM)
        );
    }
}
