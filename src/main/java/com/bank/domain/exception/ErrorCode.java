package com.bank.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ENTITY_NOT_FOUND("4001", "Entidad no encontrada"),
    ENTITY_ALREADY_EXISTS("4002", "La entidad ya existe"),
    INVALID_ENTITY_CODE("4101", "El código de entidad es obligatorio"),
    INVALID_ENTITY_NAME("4102", "El nombre de la entidad es obligatorio"),
    INVALID_ENTITY_TYPE("4103", "El tipo de entidad es obligatorio"),
    INVALID_ENTITY_CATEGORY("4104", "La categoría es obligatoria"),
    INVALID_REGISTRATION_NUMBER("4105", "El número de registro es obligatorio"),
    INVALID_DATA("4106", "Los datos de la entidad no son válidos"),
    INTERNAL_ERROR("5001", "Error interno del servidor"),
    UNKNOWN_ERROR("9999", "Error desconocido");

    private final String code;
    private final String description;
}
