package com.bank.domain.exception;

import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String details;

    public ApplicationException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.details = null;
    }

    public ApplicationException(ErrorCode errorCode, String details) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.details = details;
    }

    public String getErrorCode() {
        return errorCode.getCode();
    }

    public String getDescription() {
        return errorCode.getDescription();
    }
}
