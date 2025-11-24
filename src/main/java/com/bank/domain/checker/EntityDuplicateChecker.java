package com.bank.domain.checker;

import com.bank.domain.entity.BankingEntity;
import com.bank.domain.exception.EntityAlreadyExistsException;
import com.bank.domain.exception.EntityNotFoundException;
import com.bank.domain.port.output.BankingEntityPersistencePort;
import org.springframework.stereotype.Component;

@Component
public class EntityDuplicateChecker implements DuplicateChecker {

    private final BankingEntityPersistencePort persistencePort;

    public EntityDuplicateChecker(BankingEntityPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public void checkDuplicate(BankingEntity entity) {
        if (persistencePort.findByCode(entity.getCode()).isPresent()) {
            throw new EntityAlreadyExistsException("Código: " + entity.getCode());
        }
    }

    @Override
    public void checkDuplicateOnUpdate(Long id, BankingEntity entityData) {
        persistencePort.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("id: " + id));

        if (!persistencePort.findByCode(entityData.getCode())
                .map(e -> e.getId().equals(id))
                .orElse(true)) {
            throw new EntityAlreadyExistsException("Código: " + entityData.getCode());
        }
    }
}