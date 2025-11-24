package com.bank.application.service;

import com.bank.application.usecase.BankingEntityUseCase;
import com.bank.domain.checker.DuplicateChecker;
import com.bank.domain.entity.BankingEntity;
import com.bank.domain.entity.EntityType;
import com.bank.domain.exception.EntityNotFoundException;

import com.bank.domain.port.output.BankingEntityPersistencePort;
import com.bank.domain.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BankingEntityService implements BankingEntityUseCase {

    private final BankingEntityPersistencePort persistencePort;
    private final EntityValidator entityValidator;
    private final DuplicateChecker duplicateChecker;

    @Override
    public BankingEntity create(BankingEntity entity) {
        log.debug("Creating entity: {}", entity.getCode());
        entityValidator.validate(entity);
        duplicateChecker.checkDuplicate(entity);
        BankingEntity saved = persistencePort.save(entity);
        log.info("Entity created successfully: {}", entity.getCode());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public BankingEntity getById(Long id) {
        log.debug("Fetching entity by id: {}", id);
        return persistencePort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity Not Found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public BankingEntity getByCode(String code) {
        log.debug("Fetching entity by code: {}", code);
        return persistencePort.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Entity Not Found - code: " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankingEntity> getAll() {
        log.debug("Fetching all entities");
        return persistencePort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankingEntity> getByType(EntityType type) {
        log.debug("Fetching entities by type: {}", type);
        return persistencePort.findByType(type);
    }

    @Override
    public BankingEntity update(Long id, BankingEntity entityData) {
        log.debug("Updating entity: {}", id);
        entityValidator.validate(entityData);
        duplicateChecker.checkDuplicateOnUpdate(id, entityData);

        BankingEntity entity = persistencePort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity Not Found: " + id));

        entity.setCode(entityData.getCode());
        entity.setName(entityData.getName());
        entity.setType(entityData.getType());
        entity.setCategory(entityData.getCategory());
        entity.setRegistrationNumber(entityData.getRegistrationNumber());
        entity.setEmail(entityData.getEmail());
        entity.setPhone(entityData.getPhone());
        entity.setCountry(entityData.getCountry());
        entity.setRiskLevel(entityData.getRiskLevel());
        entity.setMonthlyVolume(entityData.getMonthlyVolume());
        entity.setStatus(entityData.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());

        BankingEntity updated = persistencePort.save(entity);
        log.info("Entity updated successfully: {}", id);
        return updated;
    }

    @Override
    public void delete(Long id) {
        log.debug("Deleting entity: {}", id);
        BankingEntity entity = persistencePort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity Not Found: " + id));
        persistencePort.deleteById(id);
        log.info("Entity deleted successfully: {}", id);
    }
}