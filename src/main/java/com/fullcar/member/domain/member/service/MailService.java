package com.fullcar.member.domain.member.service;

import com.fullcar.member.presentation.member.dto.request.EmailRequestDto;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MailService {
    void sendMail(EmailRequestDto emailRequestDto);
}
