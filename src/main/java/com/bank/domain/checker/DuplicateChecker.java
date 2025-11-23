package com.bank.domain.checker;

import com.bank.domain.entity.BankingEntity;

public interface DuplicateChecker {
    void checkDuplicate(BankingEntity entity);
    void checkDuplicateOnUpdate(Long id, BankingEntity entity);
}