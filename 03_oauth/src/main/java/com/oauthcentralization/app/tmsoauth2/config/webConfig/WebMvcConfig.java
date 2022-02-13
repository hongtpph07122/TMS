package com.oauthcentralization.app.tmsoauth2.config.webConfig;

import com.oauthcentralization.app.tmsoauth2.config.corsInterceptorConfig.CorsInterceptorConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CorsInterceptorConfig());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String[] headers = {"Origin", "Authorization", "Access-Control-Allow-Origin",
                "Access-Control-Allow-Headers", "Content-Type", "www-authenticate", "x-content-type-options", "x-xss-protection"};
        String[] exposedHeaders = {"Content-Length", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Content-Type"};
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "OPTIONS", "GET", "PUT")
                .allowedHeaders(headers)
                .exposedHeaders(exposedHeaders)
                .allowCredentials(true);
    }
}
