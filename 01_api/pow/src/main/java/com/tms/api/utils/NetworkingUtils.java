package com.tms.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Objects;

public class NetworkingUtils {

    private static final Logger logger = LoggerFactory.getLogger(NetworkingUtils.class);

    public static URL establishUrlConnection(String normalUrl) {
        try {
            return new URL(normalUrl);
        } catch (MalformedURLException error) {
            logger.error(error.getMessage(), error);
            return null;
        }
    }

    public static String snagPublicIPAsUrl(String url, String typeActions) {
        InetAddress inetAddress;
        if (ObjectUtils.isNull(typeActions)) {
            return null;
        }
        try {
            inetAddress = InetAddress.getByName(Objects.requireNonNull(establishUrlConnection(url)).getHost());
            assert false;
            switch (typeActions.toLowerCase().trim()) {
                case "1":
                case "host address":
                case "public ip":
                    return inetAddress.getHostAddress();
                case "2":
                case "hostname":
                    return inetAddress.getHostName();
                default:
                    return inetAddress.toString();
            }
        } catch (UnknownHostException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
