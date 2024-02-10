package com.fullcar.member.application.mail;

import com.fullcar.member.domain.mail.Mail;
import com.fullcar.member.domain.mail.service.MailIdService;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.infra.EmailMessage;
import com.fullcar.member.presentation.member.dto.request.EmailRequestDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MailMapper {
    private final MailIdService mailIdService;

    public Mail toEntity(Member member, Integer authCode) {
        return Mail.builder()
                .mailId(mailIdService.nextId())
                .memberId(member.getId())
                .code(authCode)
                .build();
    }

    public EmailMessage toMessageEntity(EmailRequestDto emailRequestDto) {
        return EmailMessage.builder()
                .to(emailRequestDto.getEmail())
                .subject("[FullCar] 회사 이메일 인증")
                .build();
    }
}
