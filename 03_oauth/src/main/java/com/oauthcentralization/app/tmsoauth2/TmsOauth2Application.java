package com.oauthcentralization.app.tmsoauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TmsOauth2Application {

    public static void main(String[] args) {
        SpringApplication.run(TmsOauth2Application.class, args);
    }

}
