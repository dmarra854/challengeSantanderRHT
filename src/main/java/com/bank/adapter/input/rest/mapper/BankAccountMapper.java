package com.bank.adapter.input.rest.mapper;

import com.bank.adapter.input.rest.dto.BankAccountDTO;
import com.bank.domain.entity.BankAccount;
import com.bank.domain.entity.AccountType;
import com.bank.domain.entity.AccountStatus;

/**
 * Mapper for converting between Domain and DTO.
 * Single Responsibility: Maps entities to DTOs only
 * Open/Closed: Easy to extend with new mapping rules
 */
public class BankAccountMapper {

    public static BankAccountDTO toDTO(BankAccount account) {
        if (account == null) return null;

        BankAccountDTO dto = new BankAccountDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountHolder(),
                account.getAccountType().toString(),
                account.getBalance(),
                account.getCurrency(),
                account.getCreatedAt(), // PASS THE ARGUMENT (or null)
                account.getUpdatedAt(), // PASS THE ARGUMENT (or null)
                account.getStatus().toString()
        );
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUpdatedAt(account.getUpdatedAt());
        return dto;
    }

    public static BankAccount toDomain(BankAccountDTO dto) {
        if (dto == null) return null;

        BankAccount account = new BankAccount(
                dto.getAccountNumber(),
                dto.getAccountHolder(),
                AccountType.valueOf(dto.getAccountType()),
                dto.getBalance(),
                dto.getCurrency()
        );
        account.setId(dto.getId());
        if (dto.getStatus() != null) {
            account.setStatus(AccountStatus.valueOf(dto.getStatus()));
        }
        if (dto.getCreatedAt() != null) {
            account.setCreatedAt(dto.getCreatedAt());
        }
        if (dto.getUpdatedAt() != null) {
            account.setUpdatedAt(dto.getUpdatedAt());
        }
        return account;
    }
}
