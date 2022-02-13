package com.oauthcentralization.app.tmsoauth2.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;

import java.io.*;

public class ReadUtils {

    private static final Logger logger = LoggerFactory.getLogger(ReadUtils.class);

    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException message) {
            logger.error(message.getMessage(), message);
        }
    }

    public static String snapContentAsClasspath(String path) {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = new FileInputStream(path);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = bufferedReader.readLine();
            StringBuilder stringBuilder = new StringBuilder();

            while (!ObjectUtils.isEmpty(line)) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
            return stringBuilder.toString();
        } catch (Exception message) {
            logger.error(message.getMessage(), message);
            return null;
        } finally {
            close(inputStream);
            close(bufferedReader);
        }
    }


    public static File snapClasspath(String fileName) {
        try {
            Resource resource = new ClassPathResource(fileName);
            return resource.getFile();
        } catch (IOException message) {
            logger.error(message.getMessage(), message);
            return null;
        }
    }
}
