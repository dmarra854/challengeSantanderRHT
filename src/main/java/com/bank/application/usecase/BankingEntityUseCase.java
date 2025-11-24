package com.bank.application.usecase;

import com.bank.domain.entity.BankingEntity;
import com.bank.domain.entity.EntityType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BankingEntityUseCase {
    BankingEntity create(BankingEntity entity);
    BankingEntity getById(Long id);
    BankingEntity getByCode(String code);
    List<BankingEntity> getAll();
    List<BankingEntity> getByType(EntityType type);
    BankingEntity update(Long id, BankingEntity entity);
    void delete(Long id);
}
