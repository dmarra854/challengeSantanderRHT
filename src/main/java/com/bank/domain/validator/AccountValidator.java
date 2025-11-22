package com.bank.domain.validator;

import com.bank.domain.entity.BankAccount;

/**
 * Single Responsibility: Only validates account data
 * Liskov Substitution: Can be replaced by other validators implementing the interface
 */
public interface AccountValidator {
    void validate(BankAccount account);
}