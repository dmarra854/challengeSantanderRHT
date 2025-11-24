package com.bank.adapter.input.rest.controller;

import com.bank.adapter.input.rest.dto.BankingEntityDTO;
import com.bank.adapter.input.rest.mapper.BankingEntityMapper;
import com.bank.application.usecase.BankingEntityUseCase;
import com.bank.domain.entity.BankingEntity;
import com.bank.domain.entity.EntityType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/entities")
@CrossOrigin(origins = "*")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Banking Entities", description = "Operations for managing banking entities")
public class BankingEntityRestController {

    private final BankingEntityUseCase bankingEntityUseCase;
    private final BankingEntityMapper bankingEntityMapper;

    @PostMapping
    @Operation(summary = "Create new entity", description = "Creates a new banking entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Entity created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid entity data"),
            @ApiResponse(responseCode = "409", description = "Entity already exists")
    })
    public ResponseEntity<BankingEntityDTO> create(@RequestBody BankingEntityDTO entityDTO) {
        log.info("POST /api/v1/entities - Creating entity: {}", entityDTO.getCode());
        BankingEntity entity = bankingEntityMapper.toDomain(entityDTO);
        BankingEntity createdEntity = bankingEntityUseCase.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bankingEntityMapper.toDto(createdEntity));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get entity by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity found"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    public ResponseEntity<BankingEntityDTO> getById(
            @Parameter(description = "Entity ID", example = "1", required = true)
            @PathVariable Long id) {
        log.info("GET /api/v1/entities/{} - Fetching entity", id);
        BankingEntity entity = bankingEntityUseCase.getById(id);
        return ResponseEntity.ok(bankingEntityMapper.toDto(entity));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get entity by code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity found"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    public ResponseEntity<BankingEntityDTO> getByCode(
            @Parameter(description = "Entity code", example = "ENT001", required = true)
            @PathVariable String code) {
        log.info("GET /api/v1/entities/code/{} - Fetching entity", code);
        BankingEntity entity = bankingEntityUseCase.getByCode(code);
        return ResponseEntity.ok(bankingEntityMapper.toDto(entity));
    }

    @GetMapping
    @Operation(summary = "Get all entities")
    @ApiResponse(responseCode = "200", description = "List of all entities")
    public ResponseEntity<List<BankingEntityDTO>> getAll() {
        log.info("GET /api/v1/entities - Fetching all entities");
        List<BankingEntityDTO> entities = bankingEntityUseCase.getAll()
                .stream()
                .map(bankingEntityMapper::toDto)
                .toList();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get entities by type")
    @ApiResponse(responseCode = "200", description = "List of entities")
    public ResponseEntity<List<BankingEntityDTO>> getByType(
            @Parameter(description = "Entity type", example = "CLIENTE", required = true)
            @PathVariable String type) {
        log.info("GET /api/v1/entities/type/{} - Fetching entities", type);
        List<BankingEntityDTO> entities = bankingEntityUseCase.getByType(EntityType.valueOf(type))
                .stream()
                .map(bankingEntityMapper::toDto)
                .toList();
        return ResponseEntity.ok(entities);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entity updated"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "404", description = "Entity not found"),
            @ApiResponse(responseCode = "409", description = "Code exists")
    })
    public ResponseEntity<BankingEntityDTO> update(
            @Parameter(description = "Entity ID", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody BankingEntityDTO entityDTO) {
        log.info("PUT /api/v1/entities/{} - Updating entity", id);
        BankingEntity entity = bankingEntityMapper.toDomain(entityDTO);
        BankingEntity updatedEntity = bankingEntityUseCase.update(id, entity);
        return ResponseEntity.ok(bankingEntityMapper.toDto(updatedEntity));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete entity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Entity deleted"),
            @ApiResponse(responseCode = "404", description = "Entity not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Entity ID", example = "1", required = true)
            @PathVariable Long id) {
        log.info("DELETE /api/v1/entities/{} - Deleting entity", id);
        bankingEntityUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}