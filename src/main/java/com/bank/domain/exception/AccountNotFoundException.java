package com.bank.domain.exception;

public class AccountNotFoundException extends BankAccountException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}