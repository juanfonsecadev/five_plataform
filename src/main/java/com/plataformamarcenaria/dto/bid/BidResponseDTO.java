package com.plataformamarcenaria.dto.bid;

import com.plataformamarcenaria.dto.budget.BudgetRequestResponseDTO;
import com.plataformamarcenaria.dto.user.UserResponseDTO;
import com.plataformamarcenaria.entity.Bid;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BidResponseDTO {
    private Long id;
    private UserResponseDTO carpenter;
    private BudgetRequestResponseDTO budgetRequest;
    private BigDecimal price;
    private Integer executionTimeInDays;
    private String description;
    private Bid.BidStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 