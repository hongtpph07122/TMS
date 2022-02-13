package com.oauthcentralization.app.tmsoauth2.service.serviceImpl;

import com.oauthcentralization.app.tmsoauth2.model.request.MailRequest;
import com.oauthcentralization.app.tmsoauth2.service.TMSMailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service(value = "mailService")
@Transactional
public class TMSMailServiceImpl implements TMSMailService {

    private static final Logger logger = LoggerFactory.getLogger(TMSMailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Autowired
    @Qualifier("emailConfigBean")
    private Configuration emailConfig;

    @Autowired
    public TMSMailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    @Override
    public void sendEmailWithTemplateFreeMarker(String toEmailRecipients, String subject, String body) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            mimeMessageHelper.setFrom("noreply@tmssolutions.net");
            mimeMessageHelper.setTo(toEmailRecipients);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException message) {
            logger.error(message.getMessage(), message);
        }
    }

    @Override
    public void sendEmailWithTemplateFreeMarker(MailRequest mailRequest, String templateFTL) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Template template = emailConfig.getTemplate(templateFTL); /* with extension: .ftl */
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailRequest.getModel());
            mimeMessageHelper.setFrom(mailRequest.getFrom());
            mimeMessageHelper.setTo(mailRequest.getTo());
            mimeMessageHelper.setSubject(mailRequest.getSubject());
            mimeMessageHelper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException | IOException | TemplateException message) {
            logger.error(message.getMessage(), message);
        }
    }
}
