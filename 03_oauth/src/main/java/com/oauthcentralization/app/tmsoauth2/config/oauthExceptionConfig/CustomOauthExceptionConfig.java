package com.oauthcentralization.app.tmsoauth2.config.oauthExceptionConfig;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@JsonSerialize(using = CustomOauthExceptionSerializer.class)
public class CustomOauthExceptionConfig extends OAuth2Exception {

    public CustomOauthExceptionConfig(String errorMessage) {
        super(errorMessage);
    }
}
