package com.bank.domain.validator;

import com.bank.domain.entity.BankingEntity;
import com.bank.domain.exception.ErrorCode;
import com.bank.domain.exception.InvalidEntityDataException;
import org.springframework.stereotype.Component;

@Component
public class EntityDataValidator implements EntityValidator {

    @Override
    public void validate(BankingEntity entity) {
        if (entity.getCode() == null || entity.getCode().trim().isEmpty()) {
            throw new InvalidEntityDataException(ErrorCode.INVALID_ENTITY_CODE);
        }
        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            throw new InvalidEntityDataException(ErrorCode.INVALID_ENTITY_NAME);
        }
        if (entity.getType() == null) {
            throw new InvalidEntityDataException(ErrorCode.INVALID_ENTITY_TYPE);
        }
        if (entity.getCategory() == null) {
            throw new InvalidEntityDataException(ErrorCode.INVALID_ENTITY_CATEGORY);
        }
        if (entity.getRegistrationNumber() == null || entity.getRegistrationNumber().trim().isEmpty()) {
            throw new InvalidEntityDataException(ErrorCode.INVALID_REGISTRATION_NUMBER);
        }
    }
}