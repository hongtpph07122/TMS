package com.oauthcentralization.app.tmsoauth2.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionUtils.class);

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

    public static String exchangeStackTraceToStringWithStringWriter(String error) {
        Throwable throwable = new NullPointerException(error);
        String exception;
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        exception = stringWriter.toString();
        return exception;
    }

    /**
     * @return The line number of the code that ran this method
     */
    public static int snagLineNumber() {
        return __lineOfCode();
    }

    /**
     * This methods name is ridiculous on purpose to prevent any other method
     * names in the stack trace from potentially matching this one.
     *
     * @return The line number of the code that called the method that called
     * this method(Should only be called by getLineNumber()).
     * @apiNote name function: ___8d3148796d_Xaf
     */
    private static int __lineOfCode() {
        boolean thisOne = false;
        int thisOneCountDown = 1;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : elements) {
            String methodName = element.getMethodName();
            int lineNum = element.getLineNumber();
            if (thisOne && (thisOneCountDown == 0)) {
                return lineNum;
            } else if (thisOne) {
                thisOneCountDown--;
            }
            if (methodName.equals("__lineOfCode")) {
                thisOne = true;
            }
        }
        return -1;
    }
}
