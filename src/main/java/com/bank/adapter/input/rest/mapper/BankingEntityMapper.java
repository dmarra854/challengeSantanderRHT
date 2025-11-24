package com.bank.adapter.input.rest.mapper;

import com.bank.adapter.input.rest.dto.BankingEntityDTO;
import com.bank.domain.entity.BankingEntity;
import org.springframework.stereotype.Component;

@Component
public class BankingEntityMapper {

    public BankingEntity toDomain(BankingEntityDTO dto) {
        return BankingEntity.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .type(dto.getType())
                .category(dto.getCategory())
                .registrationNumber(dto.getRegistrationNumber())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .country(dto.getCountry())
                .riskLevel(dto.getRiskLevel())
                .monthlyVolume(dto.getMonthlyVolume())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public BankingEntityDTO toDto(BankingEntity entity) {
        return new BankingEntityDTO(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getType(),
                entity.getCategory(),
                entity.getRegistrationNumber(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getCountry(),
                entity.getRiskLevel(),
                entity.getMonthlyVolume(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
