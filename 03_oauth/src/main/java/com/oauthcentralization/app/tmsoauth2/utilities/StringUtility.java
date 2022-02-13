package com.oauthcentralization.app.tmsoauth2.utilities;


import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class StringUtility {

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
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

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    public static boolean isAnyBlank(final CharSequence... css) {
        if (css != null && css.length == 0) {
            return false;
        }
        for (final CharSequence cs : css) {
            if (isBlank(cs)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether the given CharSequence contains any whitespace characters.
     * Whitespace is defined by Character.isWhitespace(char).
     * Returns true if the CharSequence is not empty and contains at least 1 (breaking) whitespace character
     */
    public static boolean containsWhitespace(final CharSequence seq) {
        if (isEmpty(seq)) {
            return false;
        }
        final int strLen = seq.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(seq.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Trim leading and trailing whitespace from the given {@code String}.
     *
     * @param str the {@code String} to check
     * @return the trimmed {@code String}
     * @see Character#isWhitespace
     */
    public static String trimAsWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int beginIndex = 0;
        int endIndex = str.length() - 1;

        while (beginIndex <= endIndex && Character.isWhitespace(str.charAt(beginIndex))) {
            beginIndex++;
        }

        while (endIndex > beginIndex && Character.isWhitespace(str.charAt(endIndex))) {
            endIndex--;
        }

        return str.substring(beginIndex, endIndex + 1);
    }

    /**
     * Trim <i>all</i> whitespace from the given {@code String}:
     * leading, trailing, and in between characters.
     *
     * @param str the {@code String} to check
     * @return the trimmed {@code String}
     * @see Character#isWhitespace
     */
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

    /**
     * Checks if the CharSequence contains only Unicode letters and space (' ').
     * null will return false An empty CharSequence (length()=0) will return true.
     */
    public static boolean isAlphaSpace(final CharSequence cs) {
        if (cs == null) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLetter(cs.charAt(i)) && cs.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the CharSequence contains only Unicode letters, digits or space (' ').
     * null will return false. An empty CharSequence (length()=0) will return true.
     */
    public static boolean isAlphanumericSpace(final CharSequence cs) {
        if (cs == null) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLetterOrDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the CharSequence contains only Unicode digits. A decimal point is not a Unicode digit and returns false.
     * null will return false. An empty CharSequence (length()=0) will return false.
     * Note that the method does not allow for a leading sign, either positive or negative. Also, if a String passes the numeric test, it may still generate a NumberFormatException when parsed by Integer.parseInt or Long.parseLong, e.g. if the value is outside the range for int or long respectively.
     */
    public static boolean isNumeric(final CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the CharSequence contains only Unicode digits or space (' '). A decimal point is not a Unicode digit and returns false.
     * null will return false. An empty CharSequence (length()=0) will return true.
     */
    public static boolean isNumericSpace(final CharSequence cs) {
        if (cs == null) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i)) && cs.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    /**
     * Searches through a given String array and returns an element that starts with the supplied startsWith string. This method ignores the case. If no match can be found then an empty String is returned
     */
    public static String startsWith(String[] array, String startsWith) {
        String lcStartsWith = startsWith.toLowerCase();
        for (String element : array) {
            if (element.toLowerCase().startsWith(lcStartsWith)) {
                return element;
            }
        }
        return "";
    }

    /**
     * Trim all occurrences of the supplied leading character from the given {@code String}.
     *
     * @param str              the {@code String} to check
     * @param leadingCharacter the leading character to be trimmed
     * @return the trimmed {@code String}
     */
    public static String trimAsLeadingCharacter(String str, char leadingCharacter) {
        if (!hasLength(str)) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str);
        while (sb.length() > 0 && sb.charAt(0) == leadingCharacter) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    /**
     * Test if the given {@code String} starts with the specified prefix,
     * * ignoring upper/lower case.
     * * @param str the {@code String} to check
     * * @param prefix the prefix to look for
     * * @see java.lang.String#startsWith
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return (str != null && prefix != null && str.length() >= prefix.length() &&
                str.regionMatches(true, 0, prefix, 0, prefix.length()));
    }

    /**
     * Quote the given {@code String} with single quotes.
     *
     * @param str the input {@code String} (e.g. "myString")
     * @return the quoted {@code String} (e.g. "'myString'"),
     * or {@code null} if the input was {@code null}
     */

    public static String quote(String str) {
        return (str != null ? "'" + str + "'" : null);
    }

    /**
     * Turn the given Object into a {@code String} with single quotes
     * if it is a {@code String}; keeping the Object as-is else.
     *
     * @param obj the input Object (e.g. "myString")
     * @return the quoted {@code String} (e.g. "'myString'"),
     * or the input object as-is if not a {@code String}
     */
    public static Object quoteIfString(Object obj) {
        return (obj instanceof String ? quote((String) obj) : obj);
    }

    /**
     * Check a String ends with another string ignoring the case.
     *
     * @param str
     * @param suffix
     * @return
     */
    public static boolean endsWithIgnoreCase(String str, String suffix) {

        if (str == null || suffix == null) {
            return false;
        }
        if (str.endsWith(suffix)) {
            return true;
        }
        if (str.length() < suffix.length()) {
            return false;
        } else {
            return str.toLowerCase().endsWith(suffix.toLowerCase());
        }
    }

    public static boolean hasLength(String str) {
        return (str != null && str.length() > 0);
    }

    /**
     * <p>
     * Checks if a String {@code str} contains Unicode digits, if yes then
     * concatenate all the digits in {@code str} and return it as a String.
     * </p>
     *
     * <p>
     * An empty ("") String will be returned if no digits found in {@code str}.
     * </p>
     *
     * @param str the String to extract digits from, may be null
     * @return String with only digits, or an empty ("") String if no digits
     * found, or {@code null} String if {@code str} is null
     * @since 3.6
     */
    public static String snagDigits(final String str) {
        if (isEmpty(str)) {
            return str;
        }
        final int sz = str.length();
        final StringBuilder strDigits = new StringBuilder(sz);
        for (int i = 0; i < sz; i++) {
            final char tempChar = str.charAt(i);
            if (Character.isDigit(tempChar)) {
                strDigits.append(tempChar);
            }
        }
        return strDigits.toString();
    }

    /**
     * <p>
     * Checks if the CharSequence contains only lowercase characters.
     * </p>
     *
     * <p>
     * {@code null} will return {@code false}. An empty CharSequence
     * (length()=0) will return {@code false}.
     * </p>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if only contains lowercase characters, and is
     * non-null
     * @since 2.5
     * @since 3.0 Changed signature from isAllLowerCase(String) to
     * isAllLowerCase(CharSequence)
     */
    public static boolean isAllLowerCase(final CharSequence cs) {
        if (cs == null || isEmpty(cs)) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLowerCase(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Checks if the CharSequence contains only uppercase characters.
     * </p>
     *
     * <p>
     * {@code null} will return {@code false}. An empty String (length()=0) will
     * return {@code false}.
     * </p>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if only contains uppercase characters, and is
     * non-null
     * @since 2.5
     * @since 3.0 Changed signature from isAllUpperCase(String) to
     * isAllUpperCase(CharSequence)
     */
    public static boolean isAllUpperCase(final CharSequence cs) {
        if (cs == null || isEmpty(cs)) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isUpperCase(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Checks if the CharSequence contains mixed casing of both uppercase and
     * lowercase characters.
     * </p>
     *
     * <p>
     * {@code null} will return {@code false}. An empty CharSequence
     * ({@code length()=0}) will return {@code false}.
     * </p>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence contains both uppercase and
     * lowercase characters
     * @since 3.5
     */
    public static boolean isMixedCase(final CharSequence cs) {
        if (isEmpty(cs) || cs.length() == 1) {
            return false;
        }
        boolean containsUppercase = false;
        boolean containsLowercase = false;
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (containsUppercase && containsLowercase) {
                return true;
            } else if (Character.isUpperCase(cs.charAt(i))) {
                containsUppercase = true;
            } else if (Character.isLowerCase(cs.charAt(i))) {
                containsLowercase = true;
            }
        }
        return containsUppercase && containsLowercase;
    }

    /**
     * Trim trailing whitespace from the given {@code String}.
     *
     * @param str the {@code String} to check
     * @return the trimmed {@code String}
     * @see Character#isWhitespace
     */
    public static String trimTrailingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Trim all occurrences of the supplied leading character from the given
     * {@code String}.
     *
     * @param str              the {@code String} to check
     * @param leadingCharacter the leading character to be trimmed
     * @return the trimmed {@code String}
     */
    public static String trimLeadingCharacter(String str, char leadingCharacter) {
        if (!hasLength(str)) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str);
        while (sb.length() > 0 && sb.charAt(0) == leadingCharacter) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    /**
     * Trim all occurrences of the supplied trailing character from the given
     * {@code String}.
     *
     * @param str               the {@code String} to check
     * @param trailingCharacter the trailing character to be trimmed
     * @return the trimmed {@code String}
     */
    public static String trimTrailingCharacter(String str, char trailingCharacter) {
        if (!hasLength(str)) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str);
        while (sb.length() > 0 && sb.charAt(sb.length() - 1) == trailingCharacter) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Iterates over the array looking for an element that, ignoring case starts
     * with the startsWith string. If a match is found then the index of the
     * matching element is returned. If no match is found then -1 is returned.
     *
     * @param array      The array of String to search through.
     * @param startsWith The String to match against
     * @return Index of the matching element, or -1 if no match is found. If
     * there are multiple matches then the index of the first match is
     * returned.
     */
    public static int indexThatStartsWith(String[] array, String startsWith) {
        String lcStartsWith = startsWith.toLowerCase();

        for (int i = 0; i < array.length; i++) {
            if (array[i].toLowerCase().startsWith(lcStartsWith)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * If a given string is longer than maxLength then it is truncated. For
     * example if str="hamburger" and maxLength=3 then "ham" will be returned.
     *
     * @param str       The str to be truncated. If this is null or equal to or
     *                  shorter than maxLength then it is returned whithout
     *                  modification.
     * @param maxLength Maximum length of the returned String. If this is less than 1,
     *                  then str is returned regardless.
     * @return A string no more than maxLength characters in length.
     */
    public static String truncate(String str, int maxLength) {
        if (str == null || str.length() <= maxLength || maxLength < 1) {
            return str;
        }

        return str.substring(0, maxLength);
    }

    public static String capitalizeEachWords(String cs) {
        if (isEmpty(cs)) {
            return "";
        }
        cs = trimAsWhitespace(cs);
        boolean first = true;
        char[] str = cs.toCharArray();
        for (int i = 0; i < str.length; ++i) {
            char character = str[i];
            str[i] = first && (i == 0 || str[i - 1] != '\'') ? Character.toUpperCase(character) : Character.toLowerCase(character);
            first = !Character.isAlphabetic(character);
        }
        return new String(str);
    }

    public static int indexOfFirstContainedCharacter(String str, String token) {
        Set<Character> set = new HashSet<>();
        for (int i = 0; i < token.length(); i++) {
            set.add(token.charAt(i));
        }
        for (int i = 0; i < str.length(); i++) {
            if (set.contains(str.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    public static String removeLastChar(String s) {
        return (isEmpty(s))
                ? null
                : (s.substring(0, s.length() - 1));
    }

    public static String removeLastCharOptional(String s) {
        return Optional.ofNullable(s)
                .filter(str -> str.length() != 0)
                .map(str -> str.substring(0, str.length() - 1))
                .orElse(s);
    }

    public static String removeLastChar(String s, int n) {
        if (!isEmpty(s)) {
            s = s.substring(0, s.length() - n);
        }
        return s;
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

    @SuppressWarnings("SameParameterValue")
    public static String[] toStringArray(Collection<?> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return null;
        }
        return collection.toArray(new String[collection.size()]);
    }


    /**
     * <p>
     * Checks if all of the CharSequences are empty (""), null or whitespace
     * only.
     * </p>
     *
     * <p>
     * Whitespace is defined by {@link Character#isWhitespace(char)}.
     * </p>
     *
     * @param css the CharSequences to check, may be null or empty
     * @return {@code true} if all of the CharSequences are empty or null or
     * whitespace only
     * @since 3.6
     */
    public static boolean isAllBlank(final CharSequence... css) {
        if (css != null && css.length == 0) {
            return true;
        }
        assert css != null;
        for (final CharSequence cs : css) {
            if (isNotBlank(cs)) {
                return false;
            }
        }
        return true;
    }

    public static int compare(final String str1, final String str2) {
        return compare(str1, str2, true);
    }

    public static int compare(final String str1, final String str2, final boolean nullIsLess) {
        if (str1.equals(str2)) {
            return 0;
        }
        if (str2 == null) {
            return nullIsLess ? 1 : -1;
        }
        return str1.compareTo(str2);
    }

    public static int compareIgnoreCase(final String str1, final String str2) {
        return compareIgnoreCase(str1, str2, true);
    }

    public static int compareIgnoreCase(final String str1, final String str2, final boolean nullIsLess) {
        if (str1.equals(str2)) {
            return 0;
        }
        if (str2 == null) {
            return nullIsLess ? 1 : -1;
        }
        return str1.compareToIgnoreCase(str2);
    }

    public static int countMatches(final CharSequence str, final char ch) {
        if (isEmpty(str)) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (ch == str.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * <p>
     * Checks if the CharSequence contains only Unicode letters.
     * </p>
     *
     * <p>
     * {@code null} will return {@code false}. An empty CharSequence
     * (length()=0) will return {@code false}.
     * </p>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if only contains letters, and is non-null
     * @since 3.0 Changed signature from isAlpha(String) to
     * isAlpha(CharSequence)
     * @since 3.0 Changed "" to return false and not true
     */
    public static boolean isAlpha(final CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isLetter(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * <p>
     * Returns the first value in the array which is not empty (""),
     * {@code null} or whitespace only.
     * </p>
     *
     * <p>
     * Whitespace is defined by {@link Character#isWhitespace(char)}.
     * </p>
     *
     * <p>
     * If all values are blank or the array is {@code null} or empty then
     * {@code null} is returned.
     * </p>
     *
     * @param <T>    the specific kind of CharSequence
     * @param values the values to test, may be {@code null} or empty
     * @return the first value from {@code values} which is not blank, or
     * {@code null} if there are no non-blank values
     * @since 3.8
     */
    @SafeVarargs
    public static <T extends CharSequence> T firstNonBlank(final T... values) {
        if (values != null) {
            for (final T val : values) {
                if (isNotBlank(val)) {
                    return val;
                }
            }
        }
        return null;
    }

    /**
     * <p>
     * Returns the first value in the array which is not empty.
     * </p>
     *
     * <p>
     * If all values are empty or the array is {@code null} or empty then
     * {@code null} is returned.
     * </p>
     *
     * @param <T>    the specific kind of CharSequence
     * @param values the values to test, may be {@code null} or empty
     * @return the first value from {@code values} which is not empty, or
     * {@code null} if there are no non-empty values
     * @since 3.8
     */
    @SafeVarargs
    public static <T extends CharSequence> T firstNonEmpty(final T... values) {
        if (values != null) {
            for (final T val : values) {
                if (isNotEmpty(val)) {
                    return val;
                }
            }
        }
        return null;
    }

    public static String trimSingleWhitespace(String str) {
        /* delete leading and trailing spaces */
        if (isEmpty(str)) {
            return "";
        }
        return str.trim().replaceAll("\\s{2,}", " ");
    }


    /**
     * 1. Traverse string str. And initialize a variable v as true.
     * 2. If str[i] == ' '. Set v as true.
     * 3. If str[i] != ' '. Check if v is true or not.
     * a) If true, copy str[i] to output string and set v as false.
     * b) If false, do nothing.
     *
     * @param str -
     */
    public static String snagFirstLetterEachWordUsingNative(String str) {
        StringBuilder result = new StringBuilder();
        // Traverse the string.
        boolean v = true;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                v = true;
            } else if (str.charAt(i) != ' ' && v) {
                result.append(str.charAt(i));
                v = false;
            }
        }
        return trimAllWhitespace(result.toString());
    }


    public static String snagFirstLetterEachWordUsingStringBuilder(String str) {
        StringBuilder charBuffer = new StringBuilder();
        /* we are splitting the input based on
           spaces (s)+ : this regular expression
           will handle scenarios where we have words
           separated by multiple spaces */
        String[] split = str.split("(\\s)+");

        for (String values : split) {
           /* charAt(0) will pick only the first character
              from the string and append to buffer */
            charBuffer.append(values.charAt(0));
        }

        return trimAllWhitespace(charBuffer.toString());
    }

    public static int countWords(String str) {
        return trimSingleWhitespace(str).split("\\s+").length;
    }

    /**
     * @param str  - input string
     * @param size - length of words , which is get from string argument
     * @description - if string argument has length > 2 then get first letter each word. Unless, get letter word by size
     */
    public static String snagFirstLetterEachWordUsingMagic(String str, int size) {
        if (isEmpty(str) || !ObjectUtils.allNotNull(size)) {
            return "";
        }

        if (str.length() < size) {
            return "";
        }

        return countWords(str) < 2 ? trimSingleWhitespace(str).substring(0, size) : snagFirstLetterEachWordUsingNative(str);
    }

    /**
     * @param str    - input string
     * @param prefix - string prefix which is removed
     */
    public static String trimLeadingCharacters(String str, String prefix) {
        if (isEmpty(str)) {
            return str;
        }
        return str.startsWith(prefix) ? str.substring(prefix.length()) : str;
    }


    /**
     * @param snakeStr - input string as form : geeks_for_geeks
     * @apiNote - output as form: GeeksForGeeks
     */
    public static String snakeToCamel(String snakeStr) {
        snakeStr = snakeStr.substring(0, 1).toUpperCase() + snakeStr.substring(1);
        /* Run a loop till string string contains underscore */
        while (snakeStr.contains("_")) {
            /* Replace the first occurrence of letter that present after the underscore, to capitalize form of next letter of underscore */
            snakeStr = snakeStr
                    .replaceFirst(
                            "_[a-z]",
                            String.valueOf(
                                    Character.toUpperCase(
                                            snakeStr.charAt(
                                                    snakeStr.indexOf("_") + 1))));
        }
        return snakeStr;
    }

    /**
     * @param originalStr - input string
     * @apiNote - output string as form opposite with original string
     */
    public static String snagCharactersIntoOppositeCase(String originalStr) {
        if (isEmpty(originalStr)) {
            return "";
        }
        StringBuilder builder = new StringBuilder(originalStr.length());
        for (int i = 0; i < originalStr.length(); i++) {
            char charAt = originalStr.charAt(i);

            if (Character.isUpperCase(charAt)) {
                builder.append(Character.toLowerCase(charAt));
            } else if (Character.isLowerCase(charAt)) {
                builder.append(Character.toUpperCase(charAt));
            } else {
                builder.append(charAt);
            }
        }
        return builder.toString();
    }


    /**
     * @param originalStr - input string
     * @apiNote - output string as form vowels {a, o, ee, u, i} is uppercase
     */
    public static String snagVowelsIntoUpperCaseCase(String originalStr) {
        if (isEmpty(originalStr)) {
            return "";
        }
        StringBuilder builder = new StringBuilder(originalStr.length());
        for (int i = 0; i < originalStr.length(); i++) {
            char charAt = originalStr.charAt(i);
            if (charAt == 'a' || charAt == 'o' || charAt == 'e' || charAt == 'u' || charAt == 'i') {
                builder.append(Character.toUpperCase(charAt));
            } else {
                builder.append(charAt);
            }
        }
        return builder.toString();
    }

    public static boolean areEqualText(String inputText, String inputSymbol) {
        if (isAnyEmpty(inputText, inputSymbol)) {
            return false;
        }
        inputText = trimSingleWhitespace(inputText).toLowerCase();
        inputSymbol = trimSingleWhitespace(inputSymbol).toLowerCase();
        return inputText.equalsIgnoreCase(inputSymbol);
    }

    public static boolean areNotEqualText(String inputText, String inputSymbol) {
        return !areEqualText(inputText, inputSymbol);
    }

    /**
     * @param camelStr - string input as form: ThisIsText
     * @apiNote - output is:  This Is Text
     */
    public static String camelToSnakeEachWord(String camelStr) {

        if (isEmpty(camelStr)) {
            return "";
        }

        String str = camelStr.replaceAll("([A-Z]+)([A-Z][a-z])", "$1 $2").replaceAll("([a-z])([A-Z])", "$1 $2");
        return capitalizeEachWords(str);
    }
}
