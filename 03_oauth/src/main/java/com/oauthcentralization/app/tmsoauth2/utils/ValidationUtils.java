package com.oauthcentralization.app.tmsoauth2.utils;


import com.oauthcentralization.app.tmsoauth2.variables.PatternRegexVariable;

import java.util.regex.Pattern;

public class ValidationUtils {

    public static boolean isVerifiedAsEmail(String emailAddress) {
        Pattern pattern = Pattern.compile(PatternRegexVariable.EMAIL_REGULAR_EXPRESSION);
        if (!ObjectUtils.allNotNull(emailAddress)) {
            return false;
        }
        return pattern.matcher(emailAddress).matches();
    }

    public static boolean isVerifiedAsPassword(String password) {
        Pattern pattern = Pattern.compile(PatternRegexVariable.PASSWORD_REGULAR_EXPRESSION);
        if (!ObjectUtils.allNotNull(password)) {
            return false;
        }
        return pattern.matcher(password).matches();
    }

}
