package com.fullcar.member.domain.member;

import com.fullcar.core.exception.CustomException;
import com.fullcar.core.exception.UnauthorizedException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.domain.auth.SocialId;
import com.fullcar.member.domain.car.CarId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, MemberId> {

    Optional<Member> findByIdAndIsDeleted(MemberId id, boolean isDeleted);

    Optional<Member> findById(MemberId id);
    Member findBySocialId(SocialId socialId);

    boolean existsBySocialId(SocialId socialId);

    Optional<Member> findByRefreshToken(String refreshToken);

    boolean existsByNickname(String nickname);

    Optional<Member> findByCarId(CarId carId);

    default Member findByIdAndIsDeletedOrThrow(MemberId id, boolean isDeleted) {
        return findByIdAndIsDeleted(id, isDeleted)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_USER));
    }

    default Member findByRefreshTokenOrThrow(String refreshToken) {
        return findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_MEMBER));
    }
}
