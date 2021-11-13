package com.sroka.grouptripsorganizer.exception;

public class InvalidPercentageBillShareException extends BadRequestException {
  public InvalidPercentageBillShareException() {
    super("error.invalidPercentage");
  }
}