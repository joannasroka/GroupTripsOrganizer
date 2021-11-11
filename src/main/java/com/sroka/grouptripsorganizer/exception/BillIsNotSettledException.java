package com.sroka.grouptripsorganizer.exception;

public class BillIsNotSettledException extends BadRequestException {
  public BillIsNotSettledException() {
    super("error.billIsNotSettled");
  }
}