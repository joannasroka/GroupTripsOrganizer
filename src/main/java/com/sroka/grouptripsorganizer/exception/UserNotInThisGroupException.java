package com.sroka.grouptripsorganizer.exception;

public class UserNotInThisGroupException extends BadRequestException{
    public UserNotInThisGroupException() {
        super("error.userNotInThisGroup");
    }
}
