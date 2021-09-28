package com.sroka.grouptripsorganizer.exception;

public class NumberOfItemsPerPageExceededLimitException extends BadRequestException {
  public NumberOfItemsPerPageExceededLimitException() {
    super("error.numberOfItemsPerPageExceededLimit");
  }
}