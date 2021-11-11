package com.sroka.grouptripsorganizer.exception;

public class UserNotInThisTripException extends BadRequestException{
    public UserNotInThisTripException() {
        super("error.userNotInThisTrip");
    }
}
