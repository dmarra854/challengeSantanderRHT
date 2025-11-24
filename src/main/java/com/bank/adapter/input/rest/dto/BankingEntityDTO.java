package com.bank.adapter.input.rest.dto;

import com.bank.domain.entity.EntityCategory;
import com.bank.domain.entity.EntityStatus;
import com.bank.domain.entity.EntityType;
import com.bank.domain.entity.RiskLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Banking Entity Information")
public class BankingEntityDTO {

    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @Schema(description = "Unique entity code", example = "ENT001", required = true)
    private String code;

    @Schema(description = "Entity name", example = "Empresa S.A.", required = true)
    private String name;

    @Schema(description = "Entity type", example = "CUSTOMER", required = true)
    private EntityType type;

    @Schema(description = "Entity category", example = "CORPORATE", required = true)
    private EntityCategory category;

    @Schema(description = "Registration number", example = "123456789", required = true)
    private String registrationNumber;

    @Schema(description = "Email address", example = "contacto@empresa.com")
    private String email;

    @Schema(description = "Phone number", example = "555-0001")
    private String phone;

    @Schema(description = "Country", example = "Argentina")
    private String country;

    @Schema(description = "Risk level", example = "LOW")
    private RiskLevel riskLevel;

    @Schema(description = "Monthly volume", example = "50000.00")
    private BigDecimal monthlyVolume;

    @Schema(description = "Entity status", example = "ACTIVE", required = true)
    private EntityStatus status;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
