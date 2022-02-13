package com.oauthcentralization.app.tmsoauth2.config.corsInterceptorConfig;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CorsInterceptorConfig extends HandlerInterceptorAdapter {

    private static final String CREDENTIALS_NAME = "Access-Control-Allow-Credentials";
    private static final String ORIGIN_NAME = "Access-Control-Allow-Origin";
    private static final String METHODS_NAME = "Access-Control-Allow-Methods";
    private static final String HEADERS_NAME = "Access-Control-Allow-Headers";
    private static final String MAX_AGE_NAME = "Access-Control-Max-Age";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setHeader(CREDENTIALS_NAME, "true");
        response.setHeader(ORIGIN_NAME, "*");
        response.setHeader(METHODS_NAME, "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader(HEADERS_NAME, "Origin, X-Requested-With, Content-Type, Accept");
        response.setHeader(MAX_AGE_NAME, "3600");
        return true;
    }
}
