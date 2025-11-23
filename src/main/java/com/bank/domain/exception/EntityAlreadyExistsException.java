package com.bank.domain.exception;

public class EntityAlreadyExistsException extends ApplicationException {
    public EntityAlreadyExistsException(String details) {
        super(ErrorCode.ENTITY_ALREADY_EXISTS, details);
    }

    public EntityAlreadyExistsException() {
        super(ErrorCode.ENTITY_ALREADY_EXISTS);
    }
}
