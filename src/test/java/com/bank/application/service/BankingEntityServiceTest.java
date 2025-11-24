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
                "Santander",
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

            BankingEntity savedEntity = testEntity;
            when(persistencePort.save(any(BankingEntity.class))).thenReturn(savedEntity);


            BankingEntity result = bankingEntityService.create(testEntity);

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo("ENT001");
            assertThat(result.getName()).isEqualTo("Santander");
            verify(entityValidator).validate(testEntity);
            verify(duplicateChecker).checkDuplicate(testEntity);
            verify(persistencePort).save(testEntity);
        }

        @Test
        @DisplayName("Should validate entity before creating")
        void shouldValidateEntityBeforeCreating() {

            doThrow(new IllegalArgumentException("Invalid entity"))
                    .when(entityValidator).validate(any(BankingEntity.class));

            assertThatThrownBy(() -> bankingEntityService.create(testEntity))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Invalid entity");
            verify(entityValidator).validate(testEntity);
            verify(persistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should check for duplicates before creating")
        void shouldCheckForDuplicatesBeforeCreating() {

            doThrow(new IllegalArgumentException("Duplicate entity"))
                    .when(duplicateChecker).checkDuplicate(any(BankingEntity.class));

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

            when(persistencePort.findById(1L)).thenReturn(Optional.of(testEntity));

            BankingEntity result = bankingEntityService.getById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getCode()).isEqualTo("ENT001");
            verify(persistencePort).findById(1L);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when ID not found")
        void shouldThrowEntityNotFoundExceptionForInvalidId() {

            when(persistencePort.findById(anyLong())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bankingEntityService.getById(999L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Entidad no encontrada");
            verify(persistencePort).findById(999L);
        }

        @Test
        @DisplayName("Should retrieve entity by code successfully")
        void shouldGetByCodeSuccessfully() {
            when(persistencePort.findByCode("ENT001")).thenReturn(Optional.of(testEntity));

            BankingEntity result = bankingEntityService.getByCode("ENT001");

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo("ENT001");
            verify(persistencePort).findByCode("ENT001");
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when code not found")
        void shouldThrowEntityNotFoundExceptionForInvalidCode() {
            when(persistencePort.findByCode("INVALID")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bankingEntityService.getByCode("INVALID"))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Entidad no encontrada");
            verify(persistencePort).findByCode("INVALID");
        }

        @Test
        @DisplayName("Should retrieve all entities successfully")
        void shouldGetAllEntitiesSuccessfully() {
            BankingEntity entity2 = new BankingEntity(
                    "ENT002",
                    "RHT",
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

            List<BankingEntity> result = bankingEntityService.getAll();

            assertThat(result).hasSize(2);
            assertThat(result).containsExactlyInAnyOrder(testEntity, entity2);
            verify(persistencePort).findAll();
        }

        @Test
        @DisplayName("Should retrieve entities by type CUSTOMER successfully")
        void shouldGetByTypeCustomerSuccessfully() {
            List<BankingEntity> entities = List.of(testEntity);
            when(persistencePort.findByType(EntityType.CUSTOMER)).thenReturn(entities);

            List<BankingEntity> result = bankingEntityService.getByType(EntityType.CUSTOMER);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getType()).isEqualTo(EntityType.CUSTOMER);
            verify(persistencePort).findByType(EntityType.CUSTOMER);
        }

        @Test
        @DisplayName("Should return empty list when no entities of type found")
        void shouldReturnEmptyListWhenNoTypeFound() {
            when(persistencePort.findByType(EntityType.REGULATOR)).thenReturn(List.of());

            List<BankingEntity> result = bankingEntityService.getByType(EntityType.REGULATOR);

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

            BankingEntity result = bankingEntityService.update(1L, updateData);

            assertThat(result).isNotNull();
            ArgumentCaptor<BankingEntity> captor = ArgumentCaptor.forClass(BankingEntity.class);
            verify(persistencePort).save(captor.capture());
            assertThat(captor.getValue().getType()).isEqualTo(EntityType.SUPPLIER);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when updating non-existent entity")
        void shouldThrowExceptionWhenUpdatingNonExistent() {
            when(persistencePort.findById(anyLong())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bankingEntityService.update(999L, testEntity))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Entidad no encontrada");

            verify(entityValidator).validate(testEntity);
            verify(duplicateChecker).checkDuplicateOnUpdate(999L, testEntity);
            verify(persistencePort).findById(999L);
            verify(persistencePort, never()).save(any());
        }

        @Test
        @DisplayName("Should validate before updating")
        void shouldValidateBeforeUpdating() {
            doThrow(new IllegalArgumentException("Invalid update data"))
                    .when(entityValidator).validate(any(BankingEntity.class));

            assertThatThrownBy(() -> bankingEntityService.update(1L, testEntity))
                    .isInstanceOf(IllegalArgumentException.class);
            verify(entityValidator).validate(testEntity);
            verify(persistencePort, never()).findById(anyLong());
        }

        @Test
        @DisplayName("Should update timestamp on entity update")
        void shouldUpdateTimestampOnEntityUpdate() {
            LocalDateTime beforeUpdate = LocalDateTime.now();
            when(persistencePort.findById(1L)).thenReturn(Optional.of(testEntity));
            when(persistencePort.save(any(BankingEntity.class))).thenReturn(testEntity);

            bankingEntityService.update(1L, testEntity);

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
            when(persistencePort.findById(1L)).thenReturn(Optional.of(testEntity));

            bankingEntityService.delete(1L);

            verify(persistencePort).findById(1L);
            verify(persistencePort).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw EntityNotFoundException when deleting non-existent entity")
        void shouldThrowExceptionWhenDeletingNonExistent() {
            when(persistencePort.findById(anyLong())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bankingEntityService.delete(999L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("Entidad no encontrada");
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

            assertThatThrownBy(() -> bankingEntityService.create(testEntity))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Entity code already exists");
        }

        @Test
        @DisplayName("Should preserve entity ID on creation")
        void shouldPreserveEntityIdOnCreation() {
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

            BankingEntity result = bankingEntityService.create(testEntity);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should maintain entity status on update")
        void shouldMaintainEntityStatusOnUpdate() {
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

            bankingEntityService.update(1L, updateData);

            ArgumentCaptor<BankingEntity> captor = ArgumentCaptor.forClass(BankingEntity.class);
            verify(persistencePort).save(captor.capture());
            assertThat(captor.getValue().getStatus()).isEqualTo(EntityStatus.SUSPENDED);
        }
    }
}