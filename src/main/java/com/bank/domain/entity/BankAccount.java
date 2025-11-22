package com.bank.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Domain entity representing a bank account.
 * Encapsulates business logic related to bank accounts.
 * Single Responsibility: Represents and manages bank account state
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "accountNumber")
public class BankAccount {

    private Long id;
    private String accountNumber;
    private String accountHolder;
    private AccountType accountType;
    private BigDecimal balance;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AccountStatus status;

    public BankAccount(String accountNumber, String accountHolder, AccountType accountType,
                       BigDecimal balance, String currency) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.accountType = accountType;
        this.balance = balance;
        this.currency = currency;
        this.status = AccountStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Business logic methods
    public boolean isActive() {
        return this.status == AccountStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = AccountStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
        log.info("Account {} deactivated", accountNumber);
    }

    public void close() {
        this.status = AccountStatus.CLOSED;
        this.updatedAt = LocalDateTime.now();
        log.info("Account {} closed", accountNumber);
    }
}
