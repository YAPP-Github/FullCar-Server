package com.fullcar.member.infra;

import com.fullcar.member.application.member.MemberMapper;
import com.fullcar.member.domain.blacklist.BlacklistRepository;
import com.fullcar.member.domain.member.EmailMessage;
import com.fullcar.member.domain.member.service.MailService;
import com.fullcar.member.presentation.member.dto.request.EmailRequestDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class MailClient implements MailService {
    private final BlacklistRepository blacklistRepository;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final MemberMapper memberMapper;

    @Override
    public void sendMail(EmailRequestDto emailRequestDto) {
        String email = emailRequestDto.getEmail();
        String emailDomain = email.substring(email.lastIndexOf("@")+1);
        blacklistRepository.findByEmailThrow(emailDomain);

        EmailMessage emailMessage = memberMapper.toEntity(emailRequestDto);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(setContext(),true);
            mimeMessageHelper.addInline("image", new ClassPathResource("static/images/fullcar_logo.png"));
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
