package com.sroka.grouptripsorganizer.exception;

public class EmailSendFailException extends RuntimeException {
    public EmailSendFailException(String message) {
        super(message);
    }
}