package com.oauthcentralization.app.tmsoauth2.controllers.base;

import com.oauthcentralization.app.tmsoauth2.service.serviceImpl.MyUserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.PostConstruct;

@SuppressWarnings({"FieldCanBeLocal"})
public abstract class BaseController {

    protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected String usernameAccess;
    protected Integer userId;
    @Autowired(required = false)
    protected MyUserDetailsImpl myUserDetails;

    @PostConstruct
    private void init() {

    }

    @ModelAttribute("handleBeforeRequest")
    public void handleBeforeRequest() {
        this.init();
        /* begin::Props */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        this.myUserDetails = (MyUserDetailsImpl) authentication.getPrincipal();
        this.usernameAccess = this.myUserDetails.getUsername();
        this.userId = this.myUserDetails.getUserId();
        /* end::Props */

    }
}
