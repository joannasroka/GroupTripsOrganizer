package com.sroka.grouptripsorganizer.exception;

public class InvalidExactAmountBillShareException extends BadRequestException {
  public InvalidExactAmountBillShareException() {
    super("error.invalidExactAmount");
  }
}