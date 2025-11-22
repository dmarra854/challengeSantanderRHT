package com.bank.domain.exception;

public abstract class BankAccountException extends RuntimeException {
    public BankAccountException(String message) {
        super(message);
    }
}