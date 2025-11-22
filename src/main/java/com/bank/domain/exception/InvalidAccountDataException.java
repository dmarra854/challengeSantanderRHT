package com.bank.domain.exception;

public class InvalidAccountDataException extends BankAccountException {
    public InvalidAccountDataException(String message) {
        super(message);
    }
}