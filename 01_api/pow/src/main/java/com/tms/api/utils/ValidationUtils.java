package com.tms.api.utils;

import com.tms.api.variable.PatternRegexVariable;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class ValidationUtils {

    public static boolean isVerifiedAsEmail(String emailAddress) {
        Pattern pattern = Pattern.compile(PatternRegexVariable.EMAIL_REGULAR_EXPRESSION);
        if (StringUtils.isEmpty(emailAddress)) {
            return false;
        }
        return pattern.matcher(emailAddress).matches();
    }

    public static boolean isVerifiedAsDate(String date) {
        Pattern pattern = Pattern.compile(PatternRegexVariable.DATE_REGULAR_EXPRESS);
        if (!ObjectUtils.allNotNull(date)) {
            return false;
        }

        if (StringUtility.countSubstring(date, "/") > 0) {
            return false;
        }

        return pattern.matcher(date).matches();
    }
}
