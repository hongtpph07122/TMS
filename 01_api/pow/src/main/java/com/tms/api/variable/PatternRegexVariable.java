package com.tms.api.variable;

public class PatternRegexVariable {

    public static final String EMAIL_REGULAR_EXPRESSION = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    /**
     * Examples of valid dates:
     * <p>
     * 2017-12-31
     * 2020-02-29
     * 2400-02-29
     * <p>
     * Examples of invalid dates:
     * <p>
     * 2017/12/31: incorrect token delimiter
     * 2018-1-1: missing leading zeroes
     * 2018-04-31: wrong days count for April
     * 2100-02-29: this year isn't leap as the value divides by 100, so February is limited to 28 days
     */
    public static final String DATE_REGULAR_EXPRESS = "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$"
            + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
            + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
            + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$";
}
