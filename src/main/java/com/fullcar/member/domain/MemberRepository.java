package com.fullcar.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<Member, MemberId> {
    Member findBySocialIdAndIsDeleted(SocialId socialId, boolean isDeleted);

    boolean existsBySocialId(SocialId socialId);
}
