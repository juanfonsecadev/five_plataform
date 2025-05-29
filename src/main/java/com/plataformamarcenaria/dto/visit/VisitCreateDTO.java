package com.plataformamarcenaria.dto.visit;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitCreateDTO {
    @NotNull(message = "ID do vendedor é obrigatório")
    private Long sellerId;

    @NotNull(message = "ID do orçamento é obrigatório")
    private Long budgetRequestId;

    @NotNull(message = "Data agendada é obrigatória")
    @Future(message = "Data deve ser no futuro")
    private LocalDateTime scheduledDate;

    private String notes;
} 