package com.fullcar.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, MemberId> {
    Optional<Member> findByClientIdAndIsDeleted(long clientId, boolean isDeleted);
}
