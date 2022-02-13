package com.tms.api.exception;

/**
 * Created by dinhanhthai on 20/04/2019.
 */
public class TMSInvalidInputException extends TMSException{
    /*public TMSInvalidInputException(String message){
        super(message);
    }*/
    public TMSInvalidInputException(ErrorMessage message){
        super(message);
    }
}
