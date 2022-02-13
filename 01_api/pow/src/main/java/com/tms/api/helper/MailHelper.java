package com.tms.api.helper;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
/**
 * Created by dinhanhthai on 21/07/2020.
 */
public class MailHelper {
    private static final Logger logger = LoggerFactory.getLogger(MailHelper.class);

    public static void sendMail(JavaMailSender javaMailSender) throws Exception{
        MimeMessage msg = javaMailSender.createMimeMessage();
        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(new String[]{ "thai.dinh@kiwi-eco.com"});
        helper.setFrom("<no-reply@tmssolutions.net>");
        helper.setSubject("TMS queue alert ");
        helper.setText("Create mail alert", true);
        logger.info("Create email ok");
        javaMailSender.send(msg);
        logger.info("Send email");

    }

    public static void sendMail(String from, String to, String subject, String body, JavaMailSender javaMailSender) throws Exception{
        MimeMessage msg = javaMailSender.createMimeMessage();
        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(new String[]{ to});
        helper.setFrom("<no-reply@tmssolutions.net>");
        helper.setSubject(subject);
        helper.setText(body, true);
        logger.info("Create email ok");
        javaMailSender.send(msg);
        logger.info("Send email");
    }
}
