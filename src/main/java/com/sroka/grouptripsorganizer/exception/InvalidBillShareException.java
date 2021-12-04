package com.sroka.grouptripsorganizer.exception;

public class InvalidBillShareException extends BadRequestException {
  public InvalidBillShareException() {
    super("error.invalidBillShare");
  }
}