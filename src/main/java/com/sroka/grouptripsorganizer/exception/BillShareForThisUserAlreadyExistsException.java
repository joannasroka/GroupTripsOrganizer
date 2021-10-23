package com.sroka.grouptripsorganizer.exception;

public class BillShareForThisUserAlreadyExistsException extends BadRequestException{
    public BillShareForThisUserAlreadyExistsException() {
        super("error.billShareForThisUserAlreadyExists");
    }
}
