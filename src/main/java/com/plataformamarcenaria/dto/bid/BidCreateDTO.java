package com.plataformamarcenaria.dto.bid;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BidCreateDTO {
    @NotNull(message = "ID do marceneiro é obrigatório")
    private Long carpenterId;

    @NotNull(message = "ID do orçamento é obrigatório")
    private Long budgetRequestId;

    @NotNull(message = "Preço é obrigatório")
    @Min(value = 0, message = "Preço deve ser maior que zero")
    private BigDecimal price;

    @NotNull(message = "Prazo de execução é obrigatório")
    @Min(value = 1, message = "Prazo deve ser de pelo menos 1 dia")
    private Integer executionTimeInDays;

    @NotBlank(message = "Descrição é obrigatória")
    private String description;
} 