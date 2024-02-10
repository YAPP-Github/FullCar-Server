package com.fullcar.member.domain.member.service;

import com.fullcar.member.domain.member.Member;
import com.fullcar.member.presentation.member.dto.request.CodeRequestDto;
import com.fullcar.member.presentation.member.dto.request.EmailRequestDto;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MailService {
    void sendMail(Member member, EmailRequestDto emailRequestDto);

    void checkMailAuthenticationCode(Member member, CodeRequestDto codeRequestDto);
}
