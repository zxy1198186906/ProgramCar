package com.volvocars.exception;

public class IllegalConditionException extends Exception {
    public IllegalConditionException(){}

    public IllegalConditionException(String message){
        super(message);
    }
}
