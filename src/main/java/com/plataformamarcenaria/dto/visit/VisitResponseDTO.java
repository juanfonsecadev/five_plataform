package com.plataformamarcenaria.dto.visit;

import com.plataformamarcenaria.dto.budget.BudgetRequestResponseDTO;
import com.plataformamarcenaria.dto.user.UserResponseDTO;
import com.plataformamarcenaria.entity.Visit;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VisitResponseDTO {
    private Long id;
    private UserResponseDTO seller;
    private BudgetRequestResponseDTO budgetRequest;
    private LocalDateTime scheduledDate;
    private Visit.VisitStatus status;
    private String notes;
    private List<String> photos;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 