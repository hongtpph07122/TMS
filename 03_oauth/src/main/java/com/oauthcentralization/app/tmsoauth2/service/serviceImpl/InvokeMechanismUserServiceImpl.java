package com.oauthcentralization.app.tmsoauth2.service.serviceImpl;

import com.oauthcentralization.app.tmsoauth2.model.request.MailRequest;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersResponse;
import com.oauthcentralization.app.tmsoauth2.service.InvokeMechanismUserService;
import com.oauthcentralization.app.tmsoauth2.service.TMSMailService;
import com.oauthcentralization.app.tmsoauth2.service.UsersService;
import com.oauthcentralization.app.tmsoauth2.utils.DateUtils;
import com.oauthcentralization.app.tmsoauth2.utils.RandomUtils;
import com.oauthcentralization.app.tmsoauth2.variables.MessageVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(value = "invokeMechanismService")
@Transactional
public class InvokeMechanismUserServiceImpl implements InvokeMechanismUserService {

    private static final Logger logger = LoggerFactory.getLogger(InvokeMechanismUserServiceImpl.class);

    private final TMSMailService mailService;
    private final UsersService usersService;

    @Autowired
    public InvokeMechanismUserServiceImpl(TMSMailService mailService, UsersService usersService) {
        this.mailService = mailService;
        this.usersService = usersService;
    }

    /**
     * @param noOfDays - no. of days password will be expired
     * @apiNote - this service will handle :
     * 1. find all accounts has password update time is expired
     * 2. updated on field : password_update_time if it is null
     * 3. send new password to account has been expired via email
     */
    @Override
    public boolean changeUPasswordMechanismAutomate(int noOfDays) {
        List<UsersResponse> usersResponses = usersService.findUsersActive();
        List<UsersResponse> usersPWDExpiredResponses = new ArrayList<>();
        List<UsersResponse> usersPWDDateIsNullResponses = new ArrayList<>();

        /* 1. find all accounts has password update time is expired */
        for (UsersResponse usersResponse : usersResponses) {
            if (!ObjectUtils.isEmpty(usersResponse.getPasswordUpdateTime())) {
                long durationExpires = DateUtils.snagDurationTypeElapsed(usersResponse.getPasswordUpdateTime(), "days");
                if (durationExpires >= noOfDays) {
                    usersPWDExpiredResponses.add(usersResponse);
                }
            } else {
                usersPWDDateIsNullResponses.add(usersResponse);
            }
        }

        /* 2. updated on field : password_update_time if it is null */
        if (!CollectionUtils.isEmpty(usersPWDDateIsNullResponses)) {
            for (UsersResponse usersResponse : usersPWDDateIsNullResponses) {
                usersService.updatePWDUpdateTimeIsNull(usersResponse.getUserId(), noOfDays);
            }
        }

        /* 3. send new password to account has been expired */
        if (!CollectionUtils.isEmpty(usersPWDExpiredResponses)) {
            for (UsersResponse usersResponse : usersPWDExpiredResponses) {
                String user_email = usersResponse.getEmail();  /* custom obj email here */
                if (!StringUtils.isEmpty(user_email)) {
                    MailRequest mailRequest = new MailRequest();
                    Map<String, String> model = new HashMap<>();
                    String user_password = RandomUtils.snapGeneratePWD(8);
                    /* update new password into db */
                    usersService.updateOneByPWDIsExpired(usersResponse.getUserId(), user_password);
                    /* set body mail request */
                    mailRequest.setFrom("noreply@tmssolutions.net");
                    mailRequest.setTo(user_email);
                    mailRequest.setSubject(MessageVariable.SUBJECT);
                    model.put("USER", usersResponse.getUsername());
                    model.put("DATE_EXPIRED", DateUtils.exchangeDateToGMT(usersResponse.getPasswordUpdateTime()));
                    model.put("NEW_PASSWORD", user_password);
                    mailRequest.setModel(model);

                    /* action: send email */
                    mailService.sendEmailWithTemplateFreeMarker(mailRequest, "reset_password.ftl");
                    logger.info("Sent email successfully... to {}", usersResponse.getUsername());
                }
            }
            return true;
        } else {
            return false;
        }
    }

}
