package com.fullcar.member.domain.blacklist;

import com.fullcar.core.exception.CustomException;
import com.fullcar.core.response.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, BlacklistId> {
    boolean existsByEmail(String email);

    default void findByEmailThrow(String email) {
        if (existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_ADDRESS_IN_BLACKLIST);
        }
    }
}
