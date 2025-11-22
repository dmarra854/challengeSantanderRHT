package com.bank.adapter.input.rest.controller;

import com.bank.adapter.input.rest.dto.BankAccountDTO;
import com.bank.adapter.input.rest.mapper.BankAccountMapper;
import com.bank.application.usecase.BankAccountUseCase;
import com.bank.domain.entity.BankAccount;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST adapter for account operations.
 * Single Responsibility: Handles HTTP requests only
 * Interface Segregation: Specific endpoints for specific operations
 * Dependency Inversion: Depends on use case abstraction
 */
@RestController
@RequestMapping("/api/v1/accounts")
@CrossOrigin(origins = "*")
@Slf4j
@RequiredArgsConstructor
public class BankAccountRestController {

    private final BankAccountUseCase bankAccountUseCase;

    @PostMapping
    public ResponseEntity<BankAccountDTO> create(@RequestBody BankAccountDTO accountDTO) {
        log.info("POST /api/v1/accounts - Creating account: {}", accountDTO.getAccountNumber());
        BankAccount account = BankAccountMapper.toDomain(accountDTO);
        BankAccount createdAccount = bankAccountUseCase.create(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(BankAccountMapper.toDTO(createdAccount));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDTO> getById(@PathVariable Long id) {
        log.info("GET /api/v1/accounts/{} - Fetching account by id", id);
        BankAccount account = bankAccountUseCase.getById(id);
        return ResponseEntity.ok(BankAccountMapper.toDTO(account));
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<BankAccountDTO> getByAccountNumber(@PathVariable String accountNumber) {
        log.info("GET /api/v1/accounts/number/{} - Fetching account by number", accountNumber);
        BankAccount account = bankAccountUseCase.getByAccountNumber(accountNumber);
        return ResponseEntity.ok(BankAccountMapper.toDTO(account));
    }

    @GetMapping
    public ResponseEntity<List<BankAccountDTO>> getAll() {
        log.info("GET /api/v1/accounts - Fetching all accounts");
        List<BankAccountDTO> accounts = bankAccountUseCase.getAll().stream()
                .map(BankAccountMapper::toDTO)
                .toList();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/holder/{accountHolder}")
    public ResponseEntity<List<BankAccountDTO>> getByAccountHolder(@PathVariable String accountHolder) {
        log.info("GET /api/v1/accounts/holder/{} - Fetching accounts by holder", accountHolder);
        List<BankAccountDTO> accounts = bankAccountUseCase.getByAccountHolder(accountHolder).stream()
                .map(BankAccountMapper::toDTO)
                .toList();
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankAccountDTO> update(@PathVariable Long id, @RequestBody BankAccountDTO accountDTO) {
        log.info("PUT /api/v1/accounts/{} - Updating account", id);
        BankAccount account = BankAccountMapper.toDomain(accountDTO);
        BankAccount updatedAccount = bankAccountUseCase.update(id, account);
        return ResponseEntity.ok(BankAccountMapper.toDTO(updatedAccount));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/v1/accounts/{} - Deleting account", id);
        bankAccountUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}