package com.bank.adapter.output.persistence.jpa;

import com.bank.domain.entity.*;
import com.bank.domain.port.output.BankingEntityPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class BankingEntityPersistenceAdapter implements BankingEntityPersistencePort {

    private final BankingEntityJpaRepository jpaRepository;

    @Override
    public BankingEntity save(BankingEntity entity) {
        log.debug("Persisting code: {}", entity.getCode());
        BankingEntityJpaEntity jpaEntity = toDomainJpa(entity);
        BankingEntityJpaEntity saved = jpaRepository.save(jpaEntity);
        log.debug("Account persisted successfully with id: {}", saved.getId());
        return toDomain(saved);
    }

    @Override
    public Optional<BankingEntity> findById(Long id) {
        log.debug("Finding entity by id: {}", id);
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<BankingEntity> findByCode(String code) {
        log.debug("Finding entity by code: {}", code);
        return jpaRepository.findByCode(code).map(this::toDomain);
    }

    @Override
    public List<BankingEntity> findAll() {
        log.debug("Finding all entities");
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<BankingEntity> findByType(EntityType type) {
        log.debug("Finding entities by type: {}", type);
        return jpaRepository.findByType(type.toString()).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting entity with id: {}", id);
        jpaRepository.deleteById(id);
        log.debug("Entity deleted successfully");
    }

    private BankingEntity toDomain(BankingEntityJpaEntity jpaEntity) {
        return new BankingEntity(
                jpaEntity.getId(),
                jpaEntity.getCode(),
                jpaEntity.getName(),
                EntityType.valueOf(jpaEntity.getType()),
                EntityCategory.valueOf(jpaEntity.getCategory()),
                jpaEntity.getRegistrationNumber(),
                jpaEntity.getEmail(),
                jpaEntity.getPhone(),
                jpaEntity.getCountry(),
                RiskLevel.valueOf(jpaEntity.getRiskLevel()),
                jpaEntity.getMonthlyVolume(),
                EntityStatus.valueOf(jpaEntity.getStatus()),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt()
        );
    }

    private BankingEntityJpaEntity toDomainJpa(BankingEntity entity) {
        return BankingEntityJpaEntity.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .type(entity.getType().toString())
                .category(entity.getCategory().toString())
                .registrationNumber(entity.getRegistrationNumber())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .country(entity.getCountry())
                .riskLevel(entity.getRiskLevel().toString())
                .monthlyVolume(entity.getMonthlyVolume())
                .status(entity.getStatus().toString())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
