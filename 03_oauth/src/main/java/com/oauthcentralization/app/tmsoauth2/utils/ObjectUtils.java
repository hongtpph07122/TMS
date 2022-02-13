package com.oauthcentralization.app.tmsoauth2.utils;

public class ObjectUtils {

    public static boolean allNotNull(final Object... values) {
        if (values == null) {
            return false;
        }
        for (final Object val : values) {
            if (val == null) {
                return false;
            }
        }
        return true;
    }
}
