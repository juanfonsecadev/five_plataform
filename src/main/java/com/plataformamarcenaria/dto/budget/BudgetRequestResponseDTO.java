package com.plataformamarcenaria.dto.budget;

import com.plataformamarcenaria.dto.address.AddressResponseDTO;
import com.plataformamarcenaria.dto.user.UserResponseDTO;
import com.plataformamarcenaria.entity.BudgetRequest;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BudgetRequestResponseDTO {
    private Long id;
    private UserResponseDTO client;
    private String description;
    private List<String> referenceImages;
    private BudgetRequest.BudgetStatus status;
    private AddressResponseDTO location;
    private BigDecimal estimatedBudget;
    private LocalDateTime desiredDeadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int totalBids;
    private BigDecimal lowestBid;
    private BigDecimal highestBid;
} 