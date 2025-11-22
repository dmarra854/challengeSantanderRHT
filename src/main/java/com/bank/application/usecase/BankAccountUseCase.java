package com.bank.application.usecase;

import com.bank.domain.entity.BankAccount;
import java.util.List;

/**
 * Input port defining use cases.
 * Interface Segregation: Specific methods for specific use cases
 * Liskov Substitution: Implementations must fulfill contract without modification
 */
public interface BankAccountUseCase {
    BankAccount create(BankAccount account);
    BankAccount getById(Long id);
    BankAccount getByAccountNumber(String accountNumber);
    List<BankAccount> getAll();
    List<BankAccount> getByAccountHolder(String accountHolder);
    BankAccount update(Long id, BankAccount account);
    void delete(Long id);
}