package com.backend.proprental.exception;

public class DuplicatedValuesException extends RuntimeException {
    public DuplicatedValuesException(String message) {
        super(message);
    }
}
