package com.api.marketplace.dtos;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequestDTO {

    // Atributos
    @NotEmpty(message = "Debe proporcionar al menos un curso para comprar")
    private List<UUID> courseUuids;
}
