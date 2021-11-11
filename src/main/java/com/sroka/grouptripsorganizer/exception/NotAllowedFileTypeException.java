package com.sroka.grouptripsorganizer.exception;

public class NotAllowedFileTypeException extends RuntimeException {
    public NotAllowedFileTypeException() {
        super("error.notAllowedFileType");
    }
}