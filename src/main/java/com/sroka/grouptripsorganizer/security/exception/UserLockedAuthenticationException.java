package com.sroka.grouptripsorganizer.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;

import static org.springframework.http.HttpStatus.LOCKED;

public class UserLockedAuthenticationException extends LockedException implements WithMessageAuthenticationException {
    public UserLockedAuthenticationException() {
        super("error.userLocked");
    }

    @Override
    public HttpStatus getHttpStatusCode() {
        return LOCKED;
    }
}