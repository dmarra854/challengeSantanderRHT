package com.bank.domain.exception;

public class AccountAlreadyExistsException extends BankAccountException {
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}
