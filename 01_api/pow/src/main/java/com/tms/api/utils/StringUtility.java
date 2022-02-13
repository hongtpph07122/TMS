package com.tms.api.utils;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtility {

    public static boolean hasLength(String str) {
        return (str != null && str.length() > 0);
    }

    public static String trimAllWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static List<String> trimAllWhitesSpace(List<String> strings) {
        List<String> result = new ArrayList<>();
        for (String element : strings) {
            result.add(trimAllWhitespace(element));
        }
        return result;
    }

    public static String trimSingleWhitespace(String str) {
        /* delete leading and trailing spaces */
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        return str.trim().replaceAll("\\s{2,}", " ");
    }

    /**
     * Returns the input argument, but ensures the first character is capitalized (if possible).
     *
     * @param in the string to uppercase the first character.
     * @return the input argument, but with the first character capitalized (if possible).
     * @since 1.2
     */
    public static String uppercaseFirstChar(String in) {
        if (in == null || in.length() == 0) {
            return in;
        }
        int length = in.length();
        StringBuilder sb = new StringBuilder(length);

        sb.append(Character.toUpperCase(in.charAt(0)));
        if (length > 1) {
            String remaining = in.substring(1);
            sb.append(remaining);
        }
        return sb.toString();
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isAnyEmpty(final CharSequence... css) {
        if (css != null && css.length == 0) {
            return false;
        }
        assert css != null;
        for (final CharSequence cs : css) {
            if (isEmpty(cs)) {
                return true;
            }
        }
        return false;
    }

    public static int countSubstring(final String text, final String substring) {
        int count = 0;

        if (isAnyEmpty(text, substring)) {
            return count;
        }

        for (int i = 0; i <= text.length() - substring.length(); i++) {
            if (substring.charAt(0) == text.charAt(i)
                    && substring.equals(text.substring(i, i + substring.length()))) {
                count++;
                i += substring.length() - 1;
            }
        }
        return count;
    }

    public static String filterPhoneNumber(String in) {
        String filterPhoneNumber;
        if (in == null || in.length() == 0) {
            return in;
        } else{
            filterPhoneNumber = in.replace("+", "")
                    .replace("-", "")
                    .replace("(", "")
                    .replace(")", "")
                    .replace(",", "")
                    .replace(" ", "")
            ;
        }

        return filterPhoneNumber;
    }

    public static boolean checkPhoneNumberIsValid(String in){
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher matcher = pattern.matcher(in);
        return matcher.matches() && in.length() >= 8 && in.length() <= 12;
    }
}
