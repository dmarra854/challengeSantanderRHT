package com.bank.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "code")
public class BankingEntity {

    private Long id;
    private String code;
    private String name;
    private EntityType type;
    private EntityCategory category;
    private String registrationNumber;
    private String email;
    private String phone;
    private String country;
    private RiskLevel riskLevel;
    private BigDecimal monthlyVolume;
    private EntityStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BankingEntity(String code, String name, EntityType type, EntityCategory category,
                         String registrationNumber, String email, String phone,
                         String country, RiskLevel riskLevel, BigDecimal monthlyVolume) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.category = category;
        this.registrationNumber = registrationNumber;
        this.email = email;
        this.phone = phone;
        this.country = country;
        this.riskLevel = riskLevel;
        this.monthlyVolume = monthlyVolume;
        this.status = EntityStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return this.status == EntityStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = EntityStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
        log.info("Entity {} deactivated", code);
    }

    public void suspend() {
        this.status = EntityStatus.SUSPENDED;
        this.updatedAt = LocalDateTime.now();
        log.info("Entity {} suspended", code);
    }
}
