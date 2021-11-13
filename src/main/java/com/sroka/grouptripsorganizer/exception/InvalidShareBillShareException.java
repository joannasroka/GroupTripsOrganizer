package com.sroka.grouptripsorganizer.exception;

public class InvalidShareBillShareException extends BadRequestException {
  public InvalidShareBillShareException() {
    super("error.invalidShare");
  }
}