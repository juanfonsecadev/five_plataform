package com.plataformamarcenaria.dto.budget;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BudgetRequestCreateDTO {
    @NotNull(message = "ID do cliente é obrigatório")
    private Long clientId;

    @NotBlank(message = "Descrição é obrigatória")
    private String description;

    private List<String> referenceImages;

    @NotNull(message = "ID do endereço é obrigatório")
    private Long locationId;

    private BigDecimal estimatedBudget;

    private LocalDateTime desiredDeadline;
} 