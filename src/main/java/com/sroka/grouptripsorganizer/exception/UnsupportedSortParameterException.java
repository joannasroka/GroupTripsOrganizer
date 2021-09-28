package com.sroka.grouptripsorganizer.exception;

public class UnsupportedSortParameterException extends BadRequestException {
  public UnsupportedSortParameterException() {
    super("error.unsupportedSortParameter");
  }
}