package com.bank.domain.port.output;

import com.bank.domain.entity.BankAccount;
import java.util.List;
import java.util.Optional;

/**
 * Output port for persistence operations.
 * Interface Segregation: Minimal interface with only necessary methods
 * Dependency Inversion: Abstracts the persistence implementation
 */
public interface BankAccountPersistencePort {
    BankAccount save(BankAccount account);
    Optional<BankAccount> findById(Long id);
    Optional<BankAccount> findByAccountNumber(String accountNumber);
    List<BankAccount> findAll();
    List<BankAccount> findByAccountHolder(String accountHolder);
    void deleteById(Long id);
}