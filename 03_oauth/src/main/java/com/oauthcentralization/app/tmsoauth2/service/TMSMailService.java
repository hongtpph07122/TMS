package com.oauthcentralization.app.tmsoauth2.service;

import com.oauthcentralization.app.tmsoauth2.model.request.MailRequest;

public interface TMSMailService {

    void sendEmailWithTemplateFreeMarker(String toEmailRecipients, String subject, String body);

    void sendEmailWithTemplateFreeMarker(MailRequest mailRequest, String templateFTL);

}
