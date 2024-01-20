package com.fullcar.member.application;

import com.fullcar.core.exception.NotFoundException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.domain.Member;
import com.fullcar.member.domain.MemberId;
import com.fullcar.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    /**
     * 회원을 식별자로 조회합니다.
     * @param memberId 조회하고자 하는 회원 아이디
     * @return 회원 엔티티
     * @throws NotFoundException 존재하지 않는 회원아이디의 경우
     */
    @Transactional(readOnly = true)
    public Member findByMemberId(MemberId memberId) {
        return memberRepository.findByIdAndIsDeleted(memberId, false)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_EXIST_USER));
    }

}
