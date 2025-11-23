package com.bank.adapter.input.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Error Response")
public class ErrorResponseDTO {

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Error description", example = "La cuenta no fue encontrada")
    private String message;
}
