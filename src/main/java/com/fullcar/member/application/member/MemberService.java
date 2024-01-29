package com.fullcar.member.application.member;

import com.fullcar.core.exception.NotFoundException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.domain.member.*;
import com.fullcar.member.presentation.member.dto.request.CompanyRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CompanyMapper companyMapper;

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

    @Transactional
    public void registerCompany(Member member, CompanyRequestDto companyRequestDto) {
        Company company = companyMapper.toEntity(companyRequestDto);
        memberRepository.findByIdAndIsDeletedOrThrow(member.getId(), false).updateCompany(company);
    }
}
