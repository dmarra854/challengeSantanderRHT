package com.bank.adapter.input.rest.controller;

import com.bank.adapter.input.rest.dto.BankAccountDTO;
import com.bank.adapter.input.rest.mapper.BankAccountMapper;
import com.bank.application.usecase.BankAccountUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST adapter for report operations.
 * Single Responsibility: Handles reporting requests only
 */
@RestController
@RequestMapping("/api/v1/reports")
@Slf4j
@RequiredArgsConstructor
public class ReportRestController {

    private final BankAccountUseCase bankAccountUseCase;

    @GetMapping("/accounts-by-holder/{accountHolder}")
    public ResponseEntity<Map<String, Object>> getReportByAccountHolder(
            @PathVariable String accountHolder) {

        log.info("GET /api/v1/reports/accounts-by-holder/{} - Generating report", accountHolder);

        List<BankAccountDTO> accounts = bankAccountUseCase.getByAccountHolder(accountHolder).stream()
                .map(BankAccountMapper::toDTO)
                .toList();

        Map<String, Object> report = new HashMap<>();
        report.put("accountHolder", accountHolder);
        report.put("totalAccounts", accounts.size());
        report.put("accounts", accounts);
        report.put("totalBalance", accounts.stream()
                .map(BankAccountDTO::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return ResponseEntity.ok(report);
    }

    @GetMapping("/general-summary")
    public ResponseEntity<Map<String, Object>> getGeneralSummary() {
        log.info("GET /api/v1/reports/general-summary - Generating general summary");

        List<BankAccountDTO> allAccounts = bankAccountUseCase.getAll().stream()
                .map(BankAccountMapper::toDTO)
                .toList();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalAccounts", allAccounts.size());
        summary.put("globalTotalBalance", allAccounts.stream()
                .map(BankAccountDTO::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        summary.put("details", allAccounts);

        return ResponseEntity.ok(summary);
    }
}