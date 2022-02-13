package com.oauthcentralization.app.tmsoauth2.utils;

import java.util.Random;

public class RandomUtils {

    private static final String PASSWORD_PREFIX_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVXYZabcdefghijklmnopqrstuvxyz!@#$%";

    public static String snapGeneratePWD(int length) {
        StringBuilder stringBuffer = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(PASSWORD_PREFIX_CHARACTERS.charAt(random.nextInt(PASSWORD_PREFIX_CHARACTERS.length())));
        }
        return stringBuffer.toString();
    }
}
