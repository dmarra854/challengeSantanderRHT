package com.bank.domain.exception;

public class EntityNotFoundException extends ApplicationException {
    public EntityNotFoundException(String details) {
        super(ErrorCode.ENTITY_NOT_FOUND, details);
    }

    public EntityNotFoundException() {
        super(ErrorCode.ENTITY_NOT_FOUND);
    }
}