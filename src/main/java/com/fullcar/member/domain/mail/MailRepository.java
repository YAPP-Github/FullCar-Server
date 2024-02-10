package com.fullcar.member.domain.mail;

import com.fullcar.member.domain.member.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository extends JpaRepository<Mail, MailId> {
    Mail findByMemberId(MemberId memberId);
}
