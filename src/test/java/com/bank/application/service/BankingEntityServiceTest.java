package com.bank.application.service;

import com.bank.domain.checker.DuplicateChecker;
import com.bank.domain.entity.BankingEntity;
import com.bank.domain.entity.EntityCategory;
import com.bank.domain.entity.EntityStatus;
import com.bank.domain.entity.EntityType;
import com.bank.domain.entity.RiskLevel;
import com.bank.domain.exception.EntityNotFoundException;
import com.bank.domain.port.output.BankingEntityPersistencePort;
import com.bank.domain.validator.EntityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Test suite for BankingEntityService.
 * Tests all CRUD operations and business logic validations.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BankingEntityService Tests")
public class BankingEntityServiceTest {

    @Mock
    private BankingEntityPersistencePort persistencePort;

    @Mock
    private EntityValidator entityValidator;

    @Mock
    private DuplicateChecker duplicateChecker;

    @InjectMocks
    private BankingEntityService bankingEntityService;

    private BankingEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new BankingEntity(
                "ENT001",
                "Test Enterprise",
                EntityType.CUSTOMER,
                EntityCategory.CORPORATE,
                "123456789",
                "test@example.com",
                "+54-123-456-7890",
                "Argentina",
                RiskLevel.LOW,
                new BigDecimal("50000.00")
        );
        testEntity.setId(1L);
        testEntity.setCreatedAt(LocalDateTime.now());
        testEntity.setUpdatedAt(LocalDateTime.now());
    }

    @Nested
    @DisplayName("Create Operation Tests")
    class CreateTests {

        @Test
        @DisplayName("Should create entity successfully with valid data")
        void shouldCreateEntitySuccessfully() {
            // Arrange
            BankingEntity savedEntity = testEntity;
            when(persistencePort.save(any(BankingEntity.class))).thenReturn(savedEntity);

            // Act
            BankingEntity result = bankingEntityService.create(testEntity);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo("ENT001");
            assertThat(result.getName()).isEqualTo("Test Enterprise");
            verify(entityValidator).validate(testEntity);
            verify(duplicateChecker).checkDuplicate(testEntity);
            verify(persistencePort).save(testEntity);
        }

        @Test
        @DisplayName("Should validate entity before creating")
        void shouldValidateEntityBeforeCreating() {
            // Arrange
            doThrow(new IllegalArgumentException("Invalid entity"))
                    .when(entityValidator).validate(any(BankingEntity.class));

            // Act & Assert
            assertThatThrownBy(() -> bankingEntityService.create(testEntity))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Invalid entity");
            verify(entityValidator).validate(testEntity);
            verify(persistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should check for duplicates before creating")
        void shouldCheckForDuplicatesBeforeCreating() {
            // Arrange
            doThrow(new IllegalArgumentException("Duplicate entity"))
                    .when(duplicateChecker).checkDuplicate(any(BankingEntity.class));

            // Act & Assert
            assertThatThrownBy(() -> bankingEntityService.create(testEntity))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Duplicate entity");
            verify(entityValidator).validate(testEntity);
            verify(duplicateChecker).checkDuplicate(testEntity);
            verify(persistencePort, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Read Operation Tests")
    class ReadTests {

        @Test
        @DisplayName("Should retrieve entity by ID successfully")
        void shouldGetByIdSuccessfully() {
            // Arrange
            when(persistencePort.findById(1L)).thenReturn(Optional.of(testEntity));

            // Act
            BankingEntity result = bankingEntityService.getById(1L);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getCode()).isEqualTo("ENT001");
            verify(persistencePort).findById(1L);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when ID not found")
        void shouldThrowEntityNotFoundExceptionForInvalidId() {
            // Arrange
            when(persistencePort.findById(anyLong())).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> bankingEntityService.getById(999L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("id: 999");
            verify(persistencePort).findById(999L);
        }

        @Test
        @DisplayName("Should retrieve entity by code successfully")
        void shouldGetByCodeSuccessfully() {
            // Arrange
            when(persistencePort.findByCode("ENT001")).thenReturn(Optional.of(testEntity));

            // Act
            BankingEntity result = bankingEntityService.getByCode("ENT001");

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo("ENT001");
            verify(persistencePort).findByCode("ENT001");
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when code not found")
        void shouldThrowEntityNotFoundExceptionForInvalidCode() {
            // Arrange
            when(persistencePort.findByCode("INVALID")).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> bankingEntityService.getByCode("INVALID"))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("code: INVALID");
            verify(persistencePort).findByCode("INVALID");
        }

        @Test
        @DisplayName("Should retrieve all entities successfully")
        void shouldGetAllEntitiesSuccessfully() {
            // Arrange
            BankingEntity entity2 = new BankingEntity(
                    "ENT002",
                    "Another Enterprise",
                    EntityType.SUPPLIER,
                    EntityCategory.INDIVIDUAL,
                    "987654321",
                    "another@example.com",
                    "+54-987-654-3210",
                    "Argentina",
                    RiskLevel.MEDIUM,
                    new BigDecimal("100000.00")
            );
            List<BankingEntity> entities = List.of(testEntity, entity2);
            when(persistencePort.findAll()).thenReturn(entities);

            // Act
            List<BankingEntity> result = bankingEntityService.getAll();

            // Assert
            assertThat(result).hasSize(2);
            assertThat(result).containsExactlyInAnyOrder(testEntity, entity2);
            verify(persistencePort).findAll();
        }

        @Test
        @DisplayName("Should retrieve entities by type CUSTOMER successfully")
        void shouldGetByTypeCustomerSuccessfully() {
            // Arrange
            List<BankingEntity> entities = List.of(testEntity);
            when(persistencePort.findByType(EntityType.CUSTOMER)).thenReturn(entities);

            // Act
            List<BankingEntity> result = bankingEntityService.getByType(EntityType.CUSTOMER);

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getType()).isEqualTo(EntityType.CUSTOMER);
            verify(persistencePort).findByType(EntityType.CUSTOMER);
        }

        @Test
        @DisplayName("Should retrieve entities by type SUPPLIER successfully")
        void shouldGetByTypeSupplierSuccessfully() {
            // Arrange
            BankingEntity supplier = new BankingEntity(
                    "ENT002",
                    "Supplier Company",
                    EntityType.SUPPLIER,
                    EntityCategory.CORPORATE,
                    "987654321",
                    "supplier@example.com",
                    "+54-987-654-3210",
                    "Argentina",
                    RiskLevel.MEDIUM,
                    new BigDecimal("100000.00")
            );
            List<BankingEntity> entities = List.of(supplier);
            when(persistencePort.findByType(EntityType.SUPPLIER)).thenReturn(entities);

            // Act
            List<BankingEntity> result = bankingEntityService.getByType(EntityType.SUPPLIER);

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getType()).isEqualTo(EntityType.SUPPLIER);
            verify(persistencePort).findByType(EntityType.SUPPLIER);
        }

        @Test
        @DisplayName("Should retrieve entities by type BRANCH successfully")
        void shouldGetByTypeBranchSuccessfully() {
            // Arrange
            BankingEntity branch = new BankingEntity(
                    "BRC001",
                    "Branch Office",
                    EntityType.BRANCH,
                    EntityCategory.CORPORATE,
                    "555555555",
                    "branch@example.com",
                    "+54-555-555-5555",
                    "Argentina",
                    RiskLevel.LOW,
                    new BigDecimal("250000.00")
            );
            List<BankingEntity> entities = List.of(branch);
            when(persistencePort.findByType(EntityType.BRANCH)).thenReturn(entities);

            // Act
            List<BankingEntity> result = bankingEntityService.getByType(EntityType.BRANCH);

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getType()).isEqualTo(EntityType.BRANCH);
            verify(persistencePort).findByType(EntityType.BRANCH);
        }

        @Test
        @DisplayName("Should retrieve entities by type INTERMEDIARY successfully")
        void shouldGetByTypeIntermediarySuccessfully() {
            // Arrange
            BankingEntity intermediary = new BankingEntity(
                    "INT001",
                    "Intermediary Entity",
                    EntityType.INTERMEDIARY,
                    EntityCategory.INDIVIDUAL,
                    "666666666",
                    "intermediary@example.com",
                    "+54-666-666-6666",
                    "Argentina",
                    RiskLevel.HIGH,
                    new BigDecimal("75000.00")
            );
            List<BankingEntity> entities = List.of(intermediary);
            when(persistencePort.findByType(EntityType.INTERMEDIARY)).thenReturn(entities);

            // Act
            List<BankingEntity> result = bankingEntityService.getByType(EntityType.INTERMEDIARY);

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getType()).isEqualTo(EntityType.INTERMEDIARY);
            verify(persistencePort).findByType(EntityType.INTERMEDIARY);
        }

        @Test
        @DisplayName("Should retrieve entities by type REGULATOR successfully")
        void shouldGetByTypeRegulatorSuccessfully() {
            // Arrange
            BankingEntity regulator = new BankingEntity(
                    "REG001",
                    "Regulatory Authority",
                    EntityType.REGULATOR,
                    EntityCategory.CORPORATE,
                    "777777777",
                    "regulator@example.com",
                    "+54-777-777-7777",
                    "Argentina",
                    RiskLevel.CRITICAL,
                    new BigDecimal("500000.00")
            );
            List<BankingEntity> entities = List.of(regulator);
            when(persistencePort.findByType(EntityType.REGULATOR)).thenReturn(entities);

            // Act
            List<BankingEntity> result = bankingEntityService.getByType(EntityType.REGULATOR);

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getType()).isEqualTo(EntityType.REGULATOR);
            verify(persistencePort).findByType(EntityType.REGULATOR);
        }

        @Test
        @DisplayName("Should retrieve entities by type PARTNER successfully")
        void shouldGetByTypePartnerSuccessfully() {
            // Arrange
            BankingEntity partner = new BankingEntity(
                    "PAR001",
                    "Business Partner",
                    EntityType.PARTNER,
                    EntityCategory.CORPORATE,
                    "888888888",
                    "partner@example.com",
                    "+54-888-888-8888",
                    "Argentina",
                    RiskLevel.MEDIUM,
                    new BigDecimal("150000.00")
            );
            List<BankingEntity> entities = List.of(partner);
            when(persistencePort.findByType(EntityType.PARTNER)).thenReturn(entities);

            // Act
            List<BankingEntity> result = bankingEntityService.getByType(EntityType.PARTNER);

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getType()).isEqualTo(EntityType.PARTNER);
            verify(persistencePort).findByType(EntityType.PARTNER);
        }

        @Test
        @DisplayName("Should return empty list when no entities of type found")
        void shouldReturnEmptyListWhenNoTypeFound() {
            // Arrange
            when(persistencePort.findByType(EntityType.REGULATOR)).thenReturn(List.of());

            // Act
            List<BankingEntity> result = bankingEntityService.getByType(EntityType.REGULATOR);

            // Assert
            assertThat(result).isEmpty();
            verify(persistencePort).findByType(EntityType.REGULATOR);
        }
    }

    @Nested
    @DisplayName("Update Operation Tests")
    class UpdateTests {

        @Test
        @DisplayName("Should update entity with SUPPLIER type successfully")
        void shouldUpdateEntityWithSupplierType() {
            // Arrange
            BankingEntity updateData = new BankingEntity(
                    "ENT001_UPDATED",
                    "Updated Supplier",
                    EntityType.SUPPLIER,
                    EntityCategory.INDIVIDUAL,
                    "111111111",
                    "updated@example.com",
                    "+54-111-111-1111",
                    "Argentina",
                    RiskLevel.HIGH,
                    new BigDecimal("75000.00")
            );

            when(persistencePort.findById(1L)).thenReturn(Optional.of(testEntity));
            when(persistencePort.save(any(BankingEntity.class))).thenReturn(testEntity);

            // Act
            BankingEntity result = bankingEntityService.update(1L, updateData);

            // Assert
            assertThat(result).isNotNull();
            ArgumentCaptor<BankingEntity> captor = ArgumentCaptor.forClass(BankingEntity.class);
            verify(persistencePort).save(captor.capture());
            assertThat(captor.getValue().getType()).isEqualTo(EntityType.SUPPLIER);
        }

        @Test
        @DisplayName("Should update entity with BRANCH type successfully")
        void shouldUpdateEntityWithBranchType() {
            // Arrange
            BankingEntity updateData = new BankingEntity(
                    "BRC001",
                    "Updated Branch",
                    EntityType.BRANCH,
                    EntityCategory.CORPORATE,
                    "222222222",
                    "branch@example.com",
                    "+54-222-222-2222",
                    "Argentina",
                    RiskLevel.LOW,
                    new BigDecimal("250000.00")
            );

            when(persistencePort.findById(1L)).thenReturn(Optional.of(testEntity));
            when(persistencePort.save(any(BankingEntity.class))).thenReturn(testEntity);

            // Act
            bankingEntityService.update(1L, updateData);

            // Assert
            ArgumentCaptor<BankingEntity> captor = ArgumentCaptor.forClass(BankingEntity.class);
            verify(persistencePort).save(captor.capture());
            assertThat(captor.getValue().getType()).isEqualTo(EntityType.BRANCH);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when updating non-existent entity")
        void shouldThrowExceptionWhenUpdatingNonExistent() {
            // Arrange
            when(persistencePort.findById(anyLong())).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> bankingEntityService.update(999L, testEntity))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("id: 999");
            verify(entityValidator).validate(testEntity);
            verify(duplicateChecker).checkDuplicateOnUpdate(999L, testEntity);
            verify(persistencePort).findById(999L);
            verify(persistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should validate before updating")
        void shouldValidateBeforeUpdating() {
            // Arrange
            doThrow(new IllegalArgumentException("Invalid update data"))
                    .when(entityValidator).validate(any(BankingEntity.class));

            // Act & Assert
            assertThatThrownBy(() -> bankingEntityService.update(1L, testEntity))
                    .isInstanceOf(IllegalArgumentException.class);
            verify(entityValidator).validate(testEntity);
            verify(persistencePort, never()).findById(anyLong());
        }

        @Test
        @DisplayName("Should update timestamp on entity update")
        void shouldUpdateTimestampOnEntityUpdate() {
            // Arrange
            LocalDateTime beforeUpdate = LocalDateTime.now();
            when(persistencePort.findById(1L)).thenReturn(Optional.of(testEntity));
            when(persistencePort.save(any(BankingEntity.class))).thenReturn(testEntity);

            // Act
            bankingEntityService.update(1L, testEntity);

            // Assert
            ArgumentCaptor<BankingEntity> captor = ArgumentCaptor.forClass(BankingEntity.class);
            verify(persistencePort).save(captor.capture());
            BankingEntity saved = captor.getValue();
            assertThat(saved.getUpdatedAt()).isNotNull();
            assertThat(saved.getUpdatedAt()).isAfterOrEqualTo(beforeUpdate);
        }
    }

    @Nested
    @DisplayName("Delete Operation Tests")
    class DeleteTests {

        @Test
        @DisplayName("Should delete entity successfully")
        void shouldDeleteEntitySuccessfully() {
            // Arrange
            when(persistencePort.findById(1L)).thenReturn(Optional.of(testEntity));

            // Act
            bankingEntityService.delete(1L);

            // Assert
            verify(persistencePort).findById(1L);
            verify(persistencePort).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when deleting non-existent entity")
        void shouldThrowExceptionWhenDeletingNonExistent() {
            // Arrange
            when(persistencePort.findById(anyLong())).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> bankingEntityService.delete(999L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("id: 999");
            verify(persistencePort).findById(999L);
            verify(persistencePort, never()).deleteById(anyLong());
        }
    }

    @Nested
    @DisplayName("Business Logic Tests")
    class BusinessLogicTests {

        @Test
        @DisplayName("Should not allow creation of duplicate entities")
        void shouldNotAllowDuplicateCreation() {
            // Arrange
            doThrow(new IllegalArgumentException("Entity code already exists"))
                    .when(duplicateChecker).checkDuplicate(any(BankingEntity.class));

            // Act & Assert
            assertThatThrownBy(() -> bankingEntityService.create(testEntity))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Entity code already exists");
        }

        @Test
        @DisplayName("Should preserve entity ID on creation")
        void shouldPreserveEntityIdOnCreation() {
            // Arrange
            testEntity.setId(null);
            BankingEntity savedEntity = new BankingEntity(
                    testEntity.getCode(),
                    testEntity.getName(),
                    testEntity.getType(),
                    testEntity.getCategory(),
                    testEntity.getRegistrationNumber(),
                    testEntity.getEmail(),
                    testEntity.getPhone(),
                    testEntity.getCountry(),
                    testEntity.getRiskLevel(),
                    testEntity.getMonthlyVolume()
            );
            savedEntity.setId(1L);
            when(persistencePort.save(any(BankingEntity.class))).thenReturn(savedEntity);

            // Act
            BankingEntity result = bankingEntityService.create(testEntity);

            // Assert
            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should maintain entity status on update")
        void shouldMaintainEntityStatusOnUpdate() {
            // Arrange
            testEntity.setStatus(EntityStatus.ACTIVE);
            BankingEntity updateData = new BankingEntity(
                    "ENT001",
                    "Updated Name",
                    EntityType.CUSTOMER,
                    EntityCategory.CORPORATE,
                    "123456789",
                    "test@example.com",
                    "+54-123-456-7890",
                    "Argentina",
                    RiskLevel.LOW,
                    new BigDecimal("50000.00")
            );
            updateData.setStatus(EntityStatus.SUSPENDED);

            when(persistencePort.findById(1L)).thenReturn(Optional.of(testEntity));
            when(persistencePort.save(any(BankingEntity.class))).thenReturn(testEntity);

            // Act
            bankingEntityService.update(1L, updateData);

            // Assert
            ArgumentCaptor<BankingEntity> captor = ArgumentCaptor.forClass(BankingEntity.class);
            verify(persistencePort).save(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo(EntityStatus.SUSPENDED);
        }
    }
}