package com.oauthcentralization.app.tmsoauth2.config.tokenEnhancerConfig;

import com.oauthcentralization.app.tmsoauth2.service.serviceImpl.MyUserDetailsImpl;
import com.oauthcentralization.app.tmsoauth2.utils.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author phuocnguyen
 * @version 1.0
 * @release on 2021/02/03
 */
public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        MyUserDetailsImpl userDetails = (MyUserDetailsImpl) authentication.getPrincipal();
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("code", HttpStatus.OK.value());
        additionalInfo.put("userType", userDetails.getUserType());
        additionalInfo.put("rolesId", userDetails.getRolesId());
        additionalInfo.put("username", userDetails.getUsername());
        additionalInfo.put("fullName", userDetails.getFullName());
        additionalInfo.put("userId", userDetails.getUserId());
        additionalInfo.put("expiredAt", DateUtils.feedStageAsString(accessToken.getExpiration()));
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }

}
