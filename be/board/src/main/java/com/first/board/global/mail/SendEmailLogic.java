package com.first.board.global.mail;

import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.BusinessException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class SendEmailLogic {

    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;


    public String makeRandomPassword(){

        StringBuilder pw = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int idx = (int) (EmailConstraints.charSet.length * Math.random());
            pw.append(EmailConstraints.charSet[idx]);
        }

        return String.valueOf(pw);
    }

    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }

    @Async("mailExecutor")
    public void sendEmail(String email, String newPassword) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject(EmailConstraints.MAIL_TITLE); // 메일 제목
            mimeMessageHelper.setText(setContext(newPassword, "password"), true); // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new BusinessException(ErrorCode.EMAIL_FAIL);
        }
    }
}