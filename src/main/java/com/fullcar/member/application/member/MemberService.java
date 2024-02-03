package com.fullcar.member.application.member;

import com.fullcar.core.exception.NotFoundException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.domain.member.*;
import com.fullcar.member.presentation.member.dto.request.CompanyRequestDto;
import com.fullcar.member.presentation.member.dto.request.EmailRequestDto;
import com.fullcar.member.presentation.member.dto.response.MemberGetResponseDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;


@Validated
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CompanyMapper companyMapper;
    private final MemberMapper memberMapper;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

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
        findByMemberId(member.getId()).updateCompany(company);
    }

    @Transactional(readOnly = true)
    public MemberGetResponseDto getMember(Member member) {
        return memberMapper.toDto(member);
    }

    public void sendMail(EmailRequestDto emailRequestDto) {
        EmailMessage emailMessage = memberMapper.toEntity(emailRequestDto);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(setContext(),true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String setContext() {
        Context context = new Context();
        return templateEngine.process("email", context);
    }
}
