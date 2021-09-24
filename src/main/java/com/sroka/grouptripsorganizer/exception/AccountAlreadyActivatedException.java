package com.sroka.grouptripsorganizer.exception;

public class AccountAlreadyActivatedException extends BadRequestException {
    public AccountAlreadyActivatedException() {
        super("error.accountAlreadyActivated");
    }
}
