package com.bank.domain.validator;

import com.bank.domain.entity.BankAccount;
import com.bank.domain.exception.InvalidAccountDataException;
import org.springframework.stereotype.Component;

/**
 * Concrete validator for account data.
 * Single Responsibility: Validates account data only
 */
@Component
public class AccountDataValidator implements AccountValidator {

    @Override
    public void validate(BankAccount account) {
        validateAccountNumber(account);
        validateAccountHolder(account);
        validateAccountType(account);
        validateBalance(account);
        validateCurrency(account);
    }

    private void validateAccountNumber(BankAccount account) {
        if (account.getAccountNumber() == null || account.getAccountNumber().trim().isEmpty()) {
            throw new InvalidAccountDataException("Account number is mandatory");
        }
    }

    private void validateAccountHolder(BankAccount account) {
        if (account.getAccountHolder() == null || account.getAccountHolder().trim().isEmpty()) {
            throw new InvalidAccountDataException("Account holder is mandatory");
        }
    }

    private void validateAccountType(BankAccount account) {
        if (account.getAccountType() == null) {
            throw new InvalidAccountDataException("Account type is mandatory");
        }
    }

    private void validateBalance(BankAccount account) {
        if (account.getBalance() == null) {
            throw new InvalidAccountDataException("Balance is mandatory");
        }
    }

    private void validateCurrency(BankAccount account) {
        if (account.getCurrency() == null || account.getCurrency().trim().isEmpty()) {
            throw new InvalidAccountDataException("Currency is mandatory");
        }
    }
}