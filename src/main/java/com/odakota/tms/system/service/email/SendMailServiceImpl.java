package com.odakota.tms.system.service.email;

import com.odakota.tms.enums.file.TemplateName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
@Slf4j
@Service
public class SendMailServiceImpl implements SendMailService {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    @Value("${spring.mail.send-from}")
    private String sendMailFrom;

    @Autowired
    public SendMailServiceImpl(TemplateEngine templateEngine,
                               JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendSimpleMail(String subject, String sendTo, TemplateName templateName, Map<String, Object> data) {
        log.info("------send simple mail start------");
        try {
            final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
            message.setSubject(subject);
            message.setFrom(sendMailFrom);
            message.setTo(sendTo);
            message.setText(buildContent(templateName, data), true);
            // send mail
            this.javaMailSender.send(mimeMessage);
        } catch (MessagingException | javax.mail.MessagingException ex) {
            log.error("Send email error: ", ex);
        }
        log.info("------send simple mail end------");
    }

    @Override
    public void sendMailWithAttachment(String subject, String sendTo, TemplateName templateName,
                                       Map<String, Object> data, String attachmentFileName, byte[] attachmentBytes,
                                       String attachmentContentType) {
        log.info("------send mail with attachment start------");
        try {
            MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            message.setSubject(subject);
            message.setFrom(sendMailFrom);
            message.setTo(sendTo);
            message.setText(buildContent(templateName, data), true);
            // add the attachment
            InputStreamSource attachmentSource = new ByteArrayResource(attachmentBytes);
            message.addAttachment(attachmentFileName, attachmentSource, attachmentContentType);
            // send mail
            this.javaMailSender.send(mimeMessage);
        } catch (MessagingException | javax.mail.MessagingException ex) {
            log.error("Send email error: ", ex);
        }
        log.info("------send mail with attachment end------");
    }

    private String buildContent(TemplateName templateName, Map<String, Object> data) {
        Context context = new Context();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        return templateEngine.process(templateName.getValue(), context);
    }
}
