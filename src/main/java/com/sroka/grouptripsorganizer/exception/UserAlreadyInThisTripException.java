package com.sroka.grouptripsorganizer.exception;

public class UserAlreadyInThisTripException extends BadRequestException{
    public UserAlreadyInThisTripException() {
        super("error.userAlreadyInThisTrip");
    }
}
