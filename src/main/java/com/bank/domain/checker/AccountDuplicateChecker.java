package com.bank.domain.checker;

import com.bank.domain.entity.BankAccount;
import com.bank.domain.exception.AccountAlreadyExistsException;
import com.bank.domain.port.output.BankAccountPersistencePort;
import org.springframework.stereotype.Component;

/**
 * Concrete duplicate checker for accounts.
 * Single Responsibility: Checks account duplicates only
 * Dependency Inversion: Depends on abstraction (BankAccountPersistencePort)
 */
@Component
public class AccountDuplicateChecker implements DuplicateChecker {

    private final BankAccountPersistencePort persistencePort;

    public AccountDuplicateChecker(BankAccountPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public void checkDuplicate(BankAccount account) {
        if (persistencePort.findByAccountNumber(account.getAccountNumber()).isPresent()) {
            throw new AccountAlreadyExistsException(
                    "Account already exists with number: " + account.getAccountNumber());
        }
    }

    @Override
    public void checkDuplicateOnUpdate(Long id, BankAccount accountData) {
        persistencePort.findById(id)
                .orElseThrow(() -> new AccountAlreadyExistsException(
                        "Account not found with id: " + id));

        if (!persistencePort.findByAccountNumber(accountData.getAccountNumber())
                .map(acc -> acc.getId().equals(id))
                .orElse(true)) {
            throw new AccountAlreadyExistsException(
                    "Another account already exists with number: " + accountData.getAccountNumber());
        }
    }
}
