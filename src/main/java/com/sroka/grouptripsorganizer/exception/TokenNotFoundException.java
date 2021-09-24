package com.sroka.grouptripsorganizer.exception;

public class TokenNotFoundException extends NotFoundException {
    public TokenNotFoundException() {
        super("error.tokenNotFound");
    }
}