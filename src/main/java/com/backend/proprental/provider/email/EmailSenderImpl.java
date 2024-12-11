package com.backend.proprental.provider.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSenderImpl implements EmailSender {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.from.personal}")
    private String fromPersonal;

    @Value("${spring.mail.from.address}")
    private String fromAddress;

    @Async
    @Override
    public void sendEmail(String to, String subject, String emailContent, List<File> attachments) {
        try {
            final var mail = javaMailSender.createMimeMessage();
            final var helper = new MimeMessageHelper(mail, true, "UTF-8");
            helper.setTo(InternetAddress.parse(to));
            helper.setSubject(subject);
            helper.setText(emailContent, true);
            helper.setSentDate(new Date());
            helper.setFrom(new InternetAddress(fromAddress, fromPersonal));
            attachments.forEach(attachment -> {
                try {
                    helper.addAttachment(attachment.getName(), attachment);
                } catch (MessagingException e) {
                    log.error("send mail error: " + e.getLocalizedMessage());
                }
            });
            log.info("javaMailSender javaMailSender sending" );
            javaMailSender.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("send mail error: " + e.getLocalizedMessage());
        }
    }

    @Async
    @Override
    public void sendEmail(String to, String subject, String content) {
        try {
            final var mail = javaMailSender.createMimeMessage();
            final var helper = new MimeMessageHelper(mail, true, "UTF-8");
            helper.setTo(InternetAddress.parse(to));
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setSentDate(new Date());
            helper.setFrom(new InternetAddress(fromAddress, fromPersonal));
            javaMailSender.send(mail);
        } catch (Exception e) {
            log.error("send mail error: " + e.getLocalizedMessage());
        }
    }
}
