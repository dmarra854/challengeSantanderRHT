package com.bank.application.service;

import com.bank.application.usecase.BankAccountUseCase;
import com.bank.domain.checker.DuplicateChecker;
import com.bank.domain.entity.BankAccount;
import com.bank.domain.exception.AccountNotFoundException;

import com.bank.domain.port.output.BankAccountPersistencePort;
import com.bank.domain.validator.AccountValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Application service orchestrating business logic.
 * Single Responsibility: Orchestrates use cases only
 * Dependency Inversion: Depends on abstractions (ports, validators, checkers)
 * Open/Closed: Easy to extend with new validators or checkers
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BankAccountService implements BankAccountUseCase {

    private final BankAccountPersistencePort persistencePort;
    private final AccountValidator accountValidator;
    private final DuplicateChecker duplicateChecker;

    @Override
    public BankAccount create(BankAccount account) {
        log.debug("Creating account: {}", account.getAccountNumber());
        accountValidator.validate(account);
        duplicateChecker.checkDuplicate(account);
        BankAccount saved = persistencePort.save(account);
        log.info("Account created successfully: {}", account.getAccountNumber());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public BankAccount getById(Long id) {
        log.debug("Fetching account by id: {}", id);
        return persistencePort.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public BankAccount getByAccountNumber(String accountNumber) {
        log.debug("Fetching account by number: {}", accountNumber);
        return persistencePort.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account not found with number: " + accountNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankAccount> getAll() {
        log.debug("Fetching all accounts");
        return persistencePort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankAccount> getByAccountHolder(String accountHolder) {
        log.debug("Fetching accounts by holder: {}", accountHolder);
        return persistencePort.findByAccountHolder(accountHolder);
    }

    @Override
    public BankAccount update(Long id, BankAccount accountData) {
        log.debug("Updating account: {}", id);
        accountValidator.validate(accountData);
        duplicateChecker.checkDuplicateOnUpdate(id, accountData);

        BankAccount account = persistencePort.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));

        account.setAccountNumber(accountData.getAccountNumber());
        account.setAccountHolder(accountData.getAccountHolder());
        account.setAccountType(accountData.getAccountType());
        account.setBalance(accountData.getBalance());
        account.setCurrency(accountData.getCurrency());
        account.setStatus(accountData.getStatus());
        account.setUpdatedAt(LocalDateTime.now());

        BankAccount updated = persistencePort.save(account);
        log.info("Account updated successfully: {}", id);
        return updated;
    }

    @Override
    public void delete(Long id) {
        log.debug("Deleting account: {}", id);
        BankAccount account = persistencePort.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
        persistencePort.deleteById(id);
        log.info("Account deleted successfully: {}", id);
    }
}