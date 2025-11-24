package com.bank.adapter.output.persistence.jpa;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Builder
    @Table(name = "banking_entities", uniqueConstraints = {
            @UniqueConstraint(columnNames = "code", name = "uk_entity_code")
    })
    public class BankingEntityJpaEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String code;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false)
        private String type;

        @Column(nullable = false)
        private String category;

        @Column(nullable = false)
        private String registrationNumber;

        @Column
        private String email;

        @Column
        private String phone;

        @Column
        private String country;

        @Column
        private String riskLevel;

        @Column
        private BigDecimal monthlyVolume;

        @Column(nullable = false)
        private String status;

        @Column(nullable = false)
        private LocalDateTime createdAt;

        @Column
        private LocalDateTime updatedAt;
    }
