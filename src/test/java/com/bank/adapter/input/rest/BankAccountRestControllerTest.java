package com.bank.adapter.input.rest;

import com.bank.adapter.input.rest.controller.BankAccountRestController;
import com.bank.adapter.input.rest.dto.BankAccountDTO;
import com.bank.domain.entity.BankAccount;
import com.bank.domain.entity.AccountType;
import com.bank.application.usecase.BankAccountUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for REST controller.
 * Tests HTTP layer and validates separation of concerns
 */
@ExtendWith(MockitoExtension.class)
class BankAccountRestControllerTest {

    @Mock
    private BankAccountUseCase bankAccountUseCase;

    @InjectMocks
    private BankAccountRestController restController;

    private BankAccount account;
    private BankAccountDTO accountDTO;

    @BeforeEach
    void setUp() {
        account = new BankAccount("123456789", "John Doe", AccountType.CHECKING,
                new BigDecimal("1000.00"), "USD");
        account.setId(1L);

        accountDTO = new BankAccountDTO(1L, "123456789", "John Doe", "CHECKING",
                new BigDecimal("1000.00"), "USD", "ACTIVE");
    }

    @Test
    void testCreateAccountEndpoint() {
        when(bankAccountUseCase.create(any(BankAccount.class))).thenReturn(account);

        ResponseEntity<BankAccountDTO> response = restController.create(accountDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("123456789", response.getBody().getAccountNumber());
        verify(bankAccountUseCase, times(1)).create(any(BankAccount.class));
    }

    @Test
    void testGetAccountByIdEndpoint() {
        when(bankAccountUseCase.getById(1L)).thenReturn(account);

        ResponseEntity<BankAccountDTO> response = restController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getAccountHolder());
    }

    @Test
    void testGetAllAccountsEndpoint() {
        List<BankAccount> accounts = new ArrayList<>();
        accounts.add(account);
        when(bankAccountUseCase.getAll()).thenReturn(accounts);

        ResponseEntity<List<BankAccountDTO>> response = restController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testDeleteAccountEndpoint() {
        doNothing().when(bankAccountUseCase).delete(1L);

        ResponseEntity<Void> response = restController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bankAccountUseCase, times(1)).delete(1L);
    }
}