package com.bank.domain.checker;

import com.bank.domain.entity.BankAccount;

/**
 * Single Responsibility: Only checks for duplicates
 * Open/Closed: Open for extension, closed for modification
 */
public interface DuplicateChecker {
    void checkDuplicate(BankAccount account);
    void checkDuplicateOnUpdate(Long id, BankAccount account);
}
