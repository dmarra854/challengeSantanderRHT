package com.bank.domain.exception;

public abstract class BankingEntityException extends RuntimeException {
    public BankingEntityException(String message) {
        super(message);
    }
}