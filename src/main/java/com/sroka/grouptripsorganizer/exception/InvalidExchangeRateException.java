package com.sroka.grouptripsorganizer.exception;

public class InvalidExchangeRateException extends BadRequestException {
    public InvalidExchangeRateException() {
        super("error.invalidExchangeRate");
    }
}
