package com.bank.domain.port.output;

import com.bank.domain.entity.BankingEntity;
import com.bank.domain.entity.EntityType;
import java.util.List;
import java.util.Optional;

public interface BankingEntityPersistencePort {
    BankingEntity save(BankingEntity entity);
    Optional<BankingEntity> findById(Long id);
    Optional<BankingEntity> findByCode(String code);
    List<BankingEntity> findAll();
    List<BankingEntity> findByType(EntityType type);
    void deleteById(Long id);
}