package com.bank.application.service;

import com.bank.domain.checker.DuplicateChecker;
import com.bank.domain.entity.BankAccount;
import com.bank.domain.entity.AccountType;
import com.bank.domain.exception.AccountNotFoundException;
import com.bank.domain.exception.InvalidAccountDataException;
import com.bank.domain.port.output.BankAccountPersistencePort;
import com.bank.domain.validator.AccountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BankAccountService.
 * Tests business logic and validates Single Responsibility
 */
@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {

    @Mock
    private BankAccountPersistencePort persistencePort;

    @Mock
    private AccountValidator accountValidator;

    @Mock
    private DuplicateChecker duplicateChecker;

    @InjectMocks
    private BankAccountService bankAccountService;

    private BankAccount account;

    @BeforeEach
    void setUp() {
        account = new BankAccount("123456789", "John Doe", AccountType.CHECKING,
                new BigDecimal("1000.00"), "USD");
        account.setId(1L);
    }

    @Test
    void testCreateAccountSuccessfully() {
        when(persistencePort.save(any(BankAccount.class))).thenReturn(account);
        doNothing().when(accountValidator).validate(any(BankAccount.class));
        doNothing().when(duplicateChecker).checkDuplicate(any(BankAccount.class));

        BankAccount result = bankAccountService.create(account);

        assertNotNull(result);
        assertEquals("123456789", result.getAccountNumber());
        assertEquals("John Doe", result.getAccountHolder());
        verify(accountValidator, times(1)).validate(any(BankAccount.class));
        verify(duplicateChecker, times(1)).checkDuplicate(any(BankAccount.class));
        verify(persistencePort, times(1)).save(any(BankAccount.class));
    }

    @Test
    void testCreateAccountWithInvalidData() {
        doThrow(new InvalidAccountDataException("Account number is mandatory"))
                .when(accountValidator).validate(any(BankAccount.class));

        assertThrows(InvalidAccountDataException.class, () -> bankAccountService.create(account));
        verify(persistencePort, never()).save(any(BankAccount.class));
    }

    @Test
    void testGetAccountByIdSuccessfully() {
        when(persistencePort.findById(1L)).thenReturn(Optional.of(account));

        BankAccount result = bankAccountService.getById(1L);

        assertNotNull(result);
        assertEquals("123456789", result.getAccountNumber());
    }

    @Test
    void testGetAccountByIdNotFound() {
        when(persistencePort.findById(999L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> bankAccountService.getById(999L));
    }

    @Test
    void testGetAccountByAccountNumber() {
        when(persistencePort.findByAccountNumber("123456789")).thenReturn(Optional.of(account));

        BankAccount result = bankAccountService.getByAccountNumber("123456789");

        assertNotNull(result);
        assertEquals("John Doe", result.getAccountHolder());
    }

    @Test
    void testUpdateAccountSuccessfully() {
        BankAccount updatedData = new BankAccount("123456789", "Jane Doe", AccountType.SAVINGS,
                new BigDecimal("2000.00"), "USD");

        when(persistencePort.findById(1L)).thenReturn(Optional.of(account));
        doNothing().when(accountValidator).validate(any(BankAccount.class));
        doNothing().when(duplicateChecker).checkDuplicateOnUpdate(1L, updatedData);
        when(persistencePort.save(any(BankAccount.class))).thenReturn(account);

        BankAccount result = bankAccountService.update(1L, updatedData);

        assertNotNull(result);
        verify(accountValidator, times(1)).validate(any(BankAccount.class));
        verify(duplicateChecker, times(1)).checkDuplicateOnUpdate(1L, updatedData);
        verify(persistencePort, times(1)).save(any(BankAccount.class));
    }

    @Test
    void testDeleteAccountSuccessfully() {
        when(persistencePort.findById(1L)).thenReturn(Optional.of(account));
        doNothing().when(persistencePort).deleteById(1L);

        assertDoesNotThrow(() -> bankAccountService.delete(1L));
        verify(persistencePort, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAccountNotFound() {
        when(persistencePort.findById(999L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> bankAccountService.delete(999L));
        verify(persistencePort, never()).deleteById(any());
    }
}