package com.sroka.grouptripsorganizer.exception;

public class NotAllowedFileTypeException extends BadRequestException {
    public NotAllowedFileTypeException() {
        super("error.notAllowedFileType");
    }
}