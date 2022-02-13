package com.oauthcentralization.app.tmsoauth2.utils;

public class FileUtils {

    public static String exactCompleteExt(String filenameAsPath) {
        int prefix = filenameAsPath.lastIndexOf(".");
        return prefix > -1 ? filenameAsPath.substring(prefix) : "";
    }
}
