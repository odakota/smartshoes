package com.odakota.tms.system.service.email;

import com.odakota.tms.enums.file.TemplateName;

import java.util.Map;

/**
 * @author haidv
 * @version 1.0
 */
public interface SendMailService {

    void sendSimpleMail(String subject, String sendTo, TemplateName templateName, Map<String, Object> data);

    void sendMailWithAttachment(String subject, String sendTo, TemplateName templateName,
                                Map<String, Object> data, String attachmentFileName, byte[] attachmentBytes,
                                String attachmentContentType);
}
