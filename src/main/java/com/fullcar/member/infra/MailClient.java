package com.fullcar.member.infra;

import com.fullcar.core.exception.CustomException;
import com.fullcar.core.response.ErrorCode;
import com.fullcar.member.application.mail.MailMapper;
import com.fullcar.member.domain.blacklist.BlacklistRepository;
import com.fullcar.member.domain.mail.Mail;
import com.fullcar.member.domain.mail.MailRepository;
import com.fullcar.member.domain.member.Member;
import com.fullcar.member.domain.member.MemberId;
import com.fullcar.member.domain.member.service.MailService;
import com.fullcar.member.presentation.member.dto.request.CodeRequestDto;
import com.fullcar.member.presentation.member.dto.request.EmailRequestDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailClient implements MailService {
    private final BlacklistRepository blacklistRepository;
    private final MailRepository mailRepository;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final MailMapper mailMapper;

    @Override
    public void sendMail(Member member, EmailRequestDto emailRequestDto) {
        Integer authNum = createRandomCode();
        String email = emailRequestDto.getEmail();
        String emailDomain = email.substring(email.lastIndexOf("@")+1);
        blacklistRepository.findByEmailThrow(emailDomain);

        if (mailRepository.findByMemberId(member.getId()) != null) {
            throw new CustomException(ErrorCode.EXISTED_CODE_IN_MAIL);
        }
        mailRepository.saveAndFlush(mailMapper.toEntity(member, authNum));

        EmailMessage emailMessage = mailMapper.toMessageEntity(emailRequestDto);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo());
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            mimeMessageHelper.setText(setContext(authNum),true);
            mimeMessageHelper.addInline("image", new ClassPathResource("static/images/fullcar_logo.png"));
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String setContext(Integer code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("email", context);
    }

    public Integer createRandomCode() {
        Random random = new Random();
        return random.nextInt(888888) + 111111;
    }

    @Override
    public void checkMailAuthenticationCode(Member member, CodeRequestDto codeRequestDto) {
        Integer code = mailRepository.findByMemberId(member.getId()).getCode();

        if (!Objects.equals(codeRequestDto.getCode(), code)) {
            throw new CustomException(ErrorCode.NOT_MATCHED_CODE);
        }
    }

    @Transactional
    @Override
    public void deleteMail(MemberId memberId) {
        Mail mail = mailRepository.findByMemberId(memberId);
        mailRepository.delete(mail);
    }
}
