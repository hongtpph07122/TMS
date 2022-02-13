package com.tms.api.exception;

/**
 * Created by dinhanhthai on 20/04/2019.
 */
public class TMSException extends Exception{
	/** The error message. */
    private final ErrorMessage errorMessage;

    /**
     * Instantiates a new epic exception.
     *
     * @param pErrorMessage
     *            the error message
     */
    public TMSException(ErrorMessage pErrorMessage) {
        super();
        errorMessage = pErrorMessage;
    }
    public TMSException(String message){
        super(message);
        errorMessage = ErrorMessage.BAD_REQUEST;
    }

    public TMSException() {
        super();
        errorMessage = null;
    }

    /**
     * Gets the error message.
     * 
     * @return the code
     */
    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
