package com.fullcar.member.domain;

import com.fullcar.core.exception.UnauthorizedException;
import com.fullcar.core.response.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findBySocialIdAndIsDeleted(SocialId socialId, boolean isDeleted);

    boolean existsBySocialId(SocialId socialId);

    Optional<Member> findByRefreshToken(String refreshToken);

    default Member findByRefreshTokenOrThrow(String refreshToken) {
        return findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_MEMBER));
    }
}
