package com.sroka.grouptripsorganizer.exception;

public class InvalidPasswordException extends BadRequestException {
    public InvalidPasswordException() {
        super("error.invalidPassword");
    }
}