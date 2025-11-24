package com.bank.domain.exception;

public class InvalidEntityDataException extends ApplicationException {
    public InvalidEntityDataException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidEntityDataException(ErrorCode errorCode, String details) {
        super(errorCode, details);
    }
}