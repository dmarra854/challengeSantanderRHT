package com.bank.adapter.output.persistence.jpa;

import com.bank.adapter.output.persistence.jpa.BankAccountJpaEntity;
import com.bank.adapter.output.persistence.jpa.BankAccountJpaRepository;
import com.bank.domain.entity.BankAccount;
import com.bank.domain.entity.AccountType;
import com.bank.domain.entity.AccountStatus;


import com.bank.domain.port.output.BankAccountPersistencePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adapter implementing persistence port.
 * Single Responsibility: Adapts domain to persistence layer
 * Liskov Substitution: Can be replaced by other persistence implementations
 * Dependency Inversion: Depends on JPA repository abstraction
 */
@Component
@Repository
public class BankAccountPersistenceAdapter implements BankAccountPersistencePort {

    private final BankAccountJpaRepository jpaRepository;
    private static final Logger log = LoggerFactory.getLogger(BankAccountPersistenceAdapter.class);

    public BankAccountPersistenceAdapter(BankAccountJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public BankAccount save(BankAccount account) {
        log.debug("Persisting account: {}", account.getAccountNumber());
        BankAccountJpaEntity jpaEntity = toDomainJpa(account);
        BankAccountJpaEntity saved = jpaRepository.save(jpaEntity);
        log.debug("Account persisted successfully with id: {}", saved.getId());
        return toDomain(saved);
    }

    @Override
    public Optional<BankAccount> findById(Long id) {
        log.debug("Finding account by id: {}", id);
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<BankAccount> findByAccountNumber(String accountNumber) {
        log.debug("Finding account by number: {}", accountNumber);
        return jpaRepository.findByAccountNumber(accountNumber).map(this::toDomain);
    }

    @Override
    public List<BankAccount> findAll() {
        log.debug("Finding all accounts");
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<BankAccount> findByAccountHolder(String accountHolder) {
        log.debug("Finding accounts by holder: {}", accountHolder);
        return jpaRepository.findByAccountHolder(accountHolder).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting account with id: {}", id);
        jpaRepository.deleteById(id);
        log.debug("Account deleted successfully");
    }

    private BankAccount toDomain(BankAccountJpaEntity jpaEntity) {
        return new BankAccount(
                jpaEntity.getId(),
                jpaEntity.getAccountNumber(),
                jpaEntity.getAccountHolder(),
                AccountType.valueOf(jpaEntity.getAccountType()),
                jpaEntity.getBalance(),
                jpaEntity.getCurrency(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt(),
                AccountStatus.valueOf(jpaEntity.getStatus())
        );
    }

    private BankAccountJpaEntity toDomainJpa(BankAccount account) {
        return new BankAccountJpaEntity(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountHolder(),
                account.getAccountType().toString(),
                account.getBalance(),
                account.getCurrency(),
                account.getCreatedAt(),
                account.getUpdatedAt(),
                account.getStatus().toString()
        );
    }
}

