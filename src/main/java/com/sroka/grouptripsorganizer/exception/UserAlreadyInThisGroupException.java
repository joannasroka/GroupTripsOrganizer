package com.sroka.grouptripsorganizer.exception;

public class UserAlreadyInThisGroupException extends BadRequestException{
    public UserAlreadyInThisGroupException() {
        super("error.userAlreadyInThisGroup");
    }
}
