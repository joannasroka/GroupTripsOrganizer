package com.sroka.grouptripsorganizer.exception;

public class DatabaseEntityNotFoundException extends NotFoundException {
    public DatabaseEntityNotFoundException() {
        super("error.resourceNotFound");
    }
}