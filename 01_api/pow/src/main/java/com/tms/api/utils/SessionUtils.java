package com.tms.api.utils;

import org.springframework.web.context.request.RequestContextHolder;

public class SessionUtils {

    public static String snapGenerateSession() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }
}
