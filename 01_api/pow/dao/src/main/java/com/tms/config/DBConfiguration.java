package com.tms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConfiguration {

    @ConfigurationProperties(prefix = "dbconfigs")
    @Bean
    public DBConfigMap dbConfigMap() {
        return new DBConfigMap();
    }
}
