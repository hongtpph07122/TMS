package com.oauthcentralization.app.tmsoauth2.variables;

public class PatternRegexVariable {

    public static final String EMAIL_REGULAR_EXPRESSION = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    /**
     * A password is considered valid if all the following constraints are satisfied:
     * <p>
     * It contains at least 8 characters and at most 20 characters.
     * It contains at least one digit.
     * It contains at least one upper case alphabet.
     * It contains at least one lower case alphabet.
     * It contains at least one special character which includes !@#$%&*()-+=^.
     * It does not contain any white space.
     */
    public static final String PASSWORD_REGULAR_EXPRESSION = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
}
