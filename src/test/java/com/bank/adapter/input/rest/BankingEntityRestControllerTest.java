package com.bank.adapter.input.rest;

import com.bank.adapter.input.rest.controller.BankingEntityRestController;
import com.bank.adapter.input.rest.dto.BankingEntityDTO;
import com.bank.adapter.input.rest.mapper.BankingEntityMapper;
import com.bank.application.usecase.BankingEntityUseCase;
import com.bank.domain.entity.BankingEntity;
import com.bank.domain.entity.EntityCategory;
import com.bank.domain.entity.EntityStatus;
import com.bank.domain.entity.EntityType;
import com.bank.domain.entity.RiskLevel;
import com.bank.domain.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("BankingEntityRestController Unit Tests")
public class BankingEntityRestControllerTest {

    @Mock
    private BankingEntityUseCase bankingEntityUseCase;

    @Mock
    private BankingEntityMapper bankingEntityMapper;

    @InjectMocks
    private BankingEntityRestController restController;

    private BankingEntity testEntity;
    private BankingEntityDTO testEntityDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        // Setup domain entity
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
        testEntity.setStatus(EntityStatus.ACTIVE);
        testEntity.setCreatedAt(now);
        testEntity.setUpdatedAt(now);

        // Setup DTO - Match your actual DTO constructor
        testEntityDTO = new BankingEntityDTO(
                1L,
                "ENT001",
                "Test Enterprise",
                EntityType.CUSTOMER,
                EntityCategory.CORPORATE,
                "123456789",
                "test@example.com",
                "+54-123-456-7890",
                "Argentina",
                RiskLevel.LOW,
                new BigDecimal("50000.00"),
                EntityStatus.ACTIVE,
                now,
                now
        );
    }

    @Nested
    @DisplayName("Create Endpoint Tests")
    class CreateEndpointTests {

        @Test
        @DisplayName("POST /api/v1/entities - Should create entity and return 201 CREATED")
        void shouldCreateEntitySuccessfully() {
            // Arrange
            when(bankingEntityMapper.toDomain(testEntityDTO)).thenReturn(testEntity);
            when(bankingEntityUseCase.create(testEntity)).thenReturn(testEntity);
            when(bankingEntityMapper.toDto(testEntity)).thenReturn(testEntityDTO);

            // Act
            ResponseEntity<BankingEntityDTO> response = restController.create(testEntityDTO);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getStatusCodeValue()).isEqualTo(201);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getCode()).isEqualTo("ENT001");
            assertThat(response.getBody().getName()).isEqualTo("Test Enterprise");

            verify(bankingEntityMapper).toDomain(testEntityDTO);
            verify(bankingEntityUseCase).create(testEntity);
            verify(bankingEntityMapper).toDto(testEntity);
        }

        @Test
        @DisplayName("POST /api/v1/entities - Should handle invalid entity data")
        void shouldHandleInvalidEntityData() {
            // Arrange
            when(bankingEntityMapper.toDomain(testEntityDTO))
                    .thenThrow(new IllegalArgumentException("Invalid entity data"));

            // Act & Assert
            assertThatThrownBy(() -> restController.create(testEntityDTO))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Invalid entity data");

            verify(bankingEntityMapper).toDomain(testEntityDTO);
            verify(bankingEntityUseCase, never()).create(any());
        }

        @Test
        @DisplayName("POST /api/v1/entities - Should handle duplicate entity")
        void shouldHandleDuplicateEntity() {
            // Arrange
            when(bankingEntityMapper.toDomain(testEntityDTO)).thenReturn(testEntity);
            when(bankingEntityUseCase.create(testEntity))
                    .thenThrow(new IllegalArgumentException("Entity already exists"));

            // Act & Assert
            assertThatThrownBy(() -> restController.create(testEntityDTO))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Entity already exists");

            verify(bankingEntityUseCase).create(testEntity);
        }
    }

    @Nested
    @DisplayName("Get by ID Endpoint Tests")
    class GetByIdEndpointTests {

        @Test
        @DisplayName("GET /api/v1/entities/{id} - Should retrieve entity and return 200 OK")
        void shouldGetByIdSuccessfully() {
            // Arrange
            when(bankingEntityUseCase.getById(1L)).thenReturn(testEntity);
            when(bankingEntityMapper.toDto(testEntity)).thenReturn(testEntityDTO);

            // Act
            ResponseEntity<BankingEntityDTO> response = restController.getById(1L);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isEqualTo(1L);
            assertThat(response.getBody().getCode()).isEqualTo("ENT001");

            verify(bankingEntityUseCase).getById(1L);
            verify(bankingEntityMapper).toDto(testEntity);
        }

        @Test
        @DisplayName("GET /api/v1/entities/{id} - Should throw exception when entity not found")
        void shouldThrowExceptionWhenEntityNotFound() {
            // Arrange
            when(bankingEntityUseCase.getById(999L))
                    .thenThrow(new EntityNotFoundException("Entidad no encontrada"));

            // Act & Assert
            assertThatThrownBy(() -> restController.getById(999L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Entidad no encontrada");

            verify(bankingEntityUseCase).getById(999L);
            verify(bankingEntityMapper, never()).toDto(any());
        }
    }

    @Nested
    @DisplayName("Get by Code Endpoint Tests")
    class GetByCodeEndpointTests {

        @Test
        @DisplayName("GET /api/v1/entities/code/{code} - Should retrieve entity by code")
        void shouldGetByCodeSuccessfully() {
            // Arrange
            when(bankingEntityUseCase.getByCode("ENT001")).thenReturn(testEntity);
            when(bankingEntityMapper.toDto(testEntity)).thenReturn(testEntityDTO);

            // Act
            ResponseEntity<BankingEntityDTO> response = restController.getByCode("ENT001");

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getCode()).isEqualTo("ENT001");

            verify(bankingEntityUseCase).getByCode("ENT001");
            verify(bankingEntityMapper).toDto(testEntity);
        }

        @Test
        @DisplayName("GET /api/v1/entities/code/{code} - Should throw exception when code not found")
        void shouldThrowExceptionWhenCodeNotFound() {
            // Arrange
            when(bankingEntityUseCase.getByCode("INVALID"))
                    .thenThrow(new EntityNotFoundException("Entidad no encontrada"));

            // Act & Assert
            assertThatThrownBy(() -> restController.getByCode("INVALID"))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Entidad no encontrada");

            verify(bankingEntityUseCase).getByCode("INVALID");
        }
    }

    @Nested
    @DisplayName("Get All Endpoint Tests")
    class GetAllEndpointTests {

        @Test
        @DisplayName("GET /api/v1/entities - Should retrieve all entities and return 200 OK")
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
            entity2.setId(2L);
            entity2.setStatus(EntityStatus.ACTIVE);

            BankingEntityDTO dto2 = new BankingEntityDTO(
                    2L,
                    "ENT002",
                    "Another Enterprise",
                    EntityType.SUPPLIER,
                    EntityCategory.INDIVIDUAL,
                    "987654321",
                    "another@example.com",
                    "+54-987-654-3210",
                    "Argentina",
                    RiskLevel.MEDIUM,
                    new BigDecimal("100000.00"),
                    EntityStatus.ACTIVE,
                    entity2.getCreatedAt(),
                    entity2.getUpdatedAt()
            );

            List<BankingEntity> entities = List.of(testEntity, entity2);
            when(bankingEntityUseCase.getAll()).thenReturn(entities);
            when(bankingEntityMapper.toDto(testEntity)).thenReturn(testEntityDTO);
            when(bankingEntityMapper.toDto(entity2)).thenReturn(dto2);

            // Act
            ResponseEntity<List<BankingEntityDTO>> response = restController.getAll();

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).hasSize(2);

            verify(bankingEntityUseCase).getAll();
            verify(bankingEntityMapper, times(2)).toDto(any());
        }

        @Test
        @DisplayName("GET /api/v1/entities - Should return empty list when no entities exist")
        void shouldReturnEmptyListWhenNoEntities() {
            // Arrange
            when(bankingEntityUseCase.getAll()).thenReturn(List.of());

            // Act
            ResponseEntity<List<BankingEntityDTO>> response = restController.getAll();

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).isEmpty();

            verify(bankingEntityUseCase).getAll();
            verify(bankingEntityMapper, never()).toDto(any());
        }
    }

    @Nested
    @DisplayName("Get by Type Endpoint Tests")
    class GetByTypeEndpointTests {

        @Test
        @DisplayName("GET /api/v1/entities/type/{type} - Should retrieve CUSTOMER type entities")
        void shouldGetByTypeCustomerSuccessfully() {
            // Arrange
            List<BankingEntity> entities = List.of(testEntity);
            when(bankingEntityUseCase.getByType(EntityType.CUSTOMER)).thenReturn(entities);
            when(bankingEntityMapper.toDto(testEntity)).thenReturn(testEntityDTO);

            // Act
            ResponseEntity<List<BankingEntityDTO>> response = restController.getByType("CUSTOMER");

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody()).hasSize(1);

            verify(bankingEntityUseCase).getByType(EntityType.CUSTOMER);
            verify(bankingEntityMapper).toDto(testEntity);
        }

        @Test
        @DisplayName("GET /api/v1/entities/type/{type} - Should retrieve SUPPLIER type entities")
        void shouldGetByTypeSupplierSuccessfully() {
            // Arrange
            BankingEntity supplier = new BankingEntity(
                    "SUP001",
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
            supplier.setId(2L);
            supplier.setStatus(EntityStatus.ACTIVE);

            BankingEntityDTO supplierDTO = new BankingEntityDTO(
                    2L,
                    "SUP001",
                    "Supplier Company",
                    EntityType.SUPPLIER,
                    EntityCategory.CORPORATE,
                    "987654321",
                    "supplier@example.com",
                    "+54-987-654-3210",
                    "Argentina",
                    RiskLevel.MEDIUM,
                    new BigDecimal("100000.00"),
                    EntityStatus.ACTIVE,
                    supplier.getCreatedAt(),
                    supplier.getUpdatedAt()
            );

            List<BankingEntity> entities = List.of(supplier);
            when(bankingEntityUseCase.getByType(EntityType.SUPPLIER)).thenReturn(entities);
            when(bankingEntityMapper.toDto(supplier)).thenReturn(supplierDTO);

            // Act
            ResponseEntity<List<BankingEntityDTO>> response = restController.getByType("SUPPLIER");

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).hasSize(1);

            verify(bankingEntityUseCase).getByType(EntityType.SUPPLIER);
        }

        @Test
        @DisplayName("GET /api/v1/entities/type/{type} - Should return empty list when no entities of type found")
        void shouldReturnEmptyListWhenNoTypeFound() {
            // Arrange
            when(bankingEntityUseCase.getByType(EntityType.REGULATOR)).thenReturn(List.of());

            // Act
            ResponseEntity<List<BankingEntityDTO>> response = restController.getByType("REGULATOR");

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEmpty();

            verify(bankingEntityUseCase).getByType(EntityType.REGULATOR);
            verify(bankingEntityMapper, never()).toDto(any());
        }
    }

    @Nested
    @DisplayName("Update Endpoint Tests")
    class UpdateEndpointTests {

        @Test
        @DisplayName("PUT /api/v1/entities/{id} - Should update entity and return 200 OK")
        void shouldUpdateEntitySuccessfully() {
            // Arrange
            BankingEntityDTO updateDTO = new BankingEntityDTO(
                    1L,
                    "ENT001_UPDATED",
                    "Updated Enterprise",
                    EntityType.SUPPLIER,
                    EntityCategory.CORPORATE,
                    "123456789",
                    "updated@example.com",
                    "+54-123-456-7890",
                    "Argentina",
                    RiskLevel.HIGH,
                    new BigDecimal("75000.00"),
                    EntityStatus.ACTIVE,
                    testEntity.getCreatedAt(),
                    LocalDateTime.now()
            );

            BankingEntity updateEntity = new BankingEntity(
                    "ENT001_UPDATED",
                    "Updated Enterprise",
                    EntityType.SUPPLIER,
                    EntityCategory.CORPORATE,
                    "123456789",
                    "updated@example.com",
                    "+54-123-456-7890",
                    "Argentina",
                    RiskLevel.HIGH,
                    new BigDecimal("75000.00")
            );
            updateEntity.setId(1L);
            updateEntity.setStatus(EntityStatus.ACTIVE);

            when(bankingEntityMapper.toDomain(updateDTO)).thenReturn(updateEntity);
            when(bankingEntityUseCase.update(1L, updateEntity)).thenReturn(updateEntity);
            when(bankingEntityMapper.toDto(updateEntity)).thenReturn(updateDTO);

            // Act
            ResponseEntity<BankingEntityDTO> response = restController.update(1L, updateDTO);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getStatusCodeValue()).isEqualTo(200);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getCode()).isEqualTo("ENT001_UPDATED");

            verify(bankingEntityMapper).toDomain(updateDTO);
            verify(bankingEntityUseCase).update(1L, updateEntity);
            verify(bankingEntityMapper).toDto(updateEntity);
        }

        @Test
        @DisplayName("PUT /api/v1/entities/{id} - Should throw exception when entity not found")
        void shouldThrowExceptionWhenUpdatingNonExistent() {
            // Arrange
            BankingEntityDTO updateDTO = new BankingEntityDTO();
            when(bankingEntityMapper.toDomain(updateDTO)).thenReturn(testEntity);
            when(bankingEntityUseCase.update(999L, testEntity))
                    .thenThrow(new EntityNotFoundException("Entidad no encontrada"));

            // Act & Assert
            assertThatThrownBy(() -> restController.update(999L, updateDTO))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Entidad no encontrada");

            verify(bankingEntityUseCase).update(999L, testEntity);
            verify(bankingEntityMapper, never()).toDto(any());
        }
    }

    @Nested
    @DisplayName("Delete Endpoint Tests")
    class DeleteEndpointTests {

        @Test
        @DisplayName("DELETE /api/v1/entities/{id} - Should delete entity and return 204 NO_CONTENT")
        void shouldDeleteEntitySuccessfully() {
            // Arrange
            doNothing().when(bankingEntityUseCase).delete(1L);

            // Act
            ResponseEntity<Void> response = restController.delete(1L);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(response.getStatusCodeValue()).isEqualTo(204);
            assertThat(response.getBody()).isNull();

            verify(bankingEntityUseCase).delete(1L);
        }

        @Test
        @DisplayName("DELETE /api/v1/entities/{id} - Should throw exception when entity not found")
        void shouldThrowExceptionWhenDeletingNonExistent() {
            // Arrange
            doThrow(new EntityNotFoundException("Entidad no encontrada"))
                    .when(bankingEntityUseCase).delete(999L);

            // Act & Assert
            assertThatThrownBy(() -> restController.delete(999L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Entidad no encontrada");

            verify(bankingEntityUseCase).delete(999L);
        }
    }

    @Nested
    @DisplayName("HTTP Status Code Tests")
    class HttpStatusTests {

        @Test
        @DisplayName("Should return 201 CREATED for successful POST")
        void shouldReturn201ForCreate() {
            // Arrange
            when(bankingEntityMapper.toDomain(testEntityDTO)).thenReturn(testEntity);
            when(bankingEntityUseCase.create(testEntity)).thenReturn(testEntity);
            when(bankingEntityMapper.toDto(testEntity)).thenReturn(testEntityDTO);

            // Act
            ResponseEntity<BankingEntityDTO> response = restController.create(testEntityDTO);

            // Assert
            assertThat(response.getStatusCode().value()).isEqualTo(201);
        }

        @Test
        @DisplayName("Should return 200 OK for successful GET")
        void shouldReturn200ForGet() {
            // Arrange
            when(bankingEntityUseCase.getById(1L)).thenReturn(testEntity);
            when(bankingEntityMapper.toDto(testEntity)).thenReturn(testEntityDTO);

            // Act
            ResponseEntity<BankingEntityDTO> response = restController.getById(1L);

            // Assert
            assertThat(response.getStatusCode().value()).isEqualTo(200);
        }

        @Test
        @DisplayName("Should return 204 NO_CONTENT for successful DELETE")
        void shouldReturn204ForDelete() {
            // Arrange
            doNothing().when(bankingEntityUseCase).delete(1L);

            // Act
            ResponseEntity<Void> response = restController.delete(1L);

            // Assert
            assertThat(response.getStatusCode().value()).isEqualTo(204);
        }
    }
}