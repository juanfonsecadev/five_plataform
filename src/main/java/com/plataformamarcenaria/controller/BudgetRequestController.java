package com.plataformamarcenaria.controller;

import com.plataformamarcenaria.dto.budget.BudgetRequestCreateDTO;
import com.plataformamarcenaria.dto.budget.BudgetRequestResponseDTO;
import com.plataformamarcenaria.entity.BudgetRequest;
import com.plataformamarcenaria.service.BudgetRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budget-requests")
@RequiredArgsConstructor
public class BudgetRequestController {
    private final BudgetRequestService budgetRequestService;

    @PostMapping
    public ResponseEntity<BudgetRequestResponseDTO> createBudgetRequest(
            @Valid @RequestBody BudgetRequestCreateDTO createDTO) {
        return new ResponseEntity<>(budgetRequestService.createBudgetRequest(createDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetRequestResponseDTO> getBudgetRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(budgetRequestService.getBudgetRequestById(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<BudgetRequestResponseDTO>> getBudgetRequestsByClientId(
            @PathVariable Long clientId) {
        return ResponseEntity.ok(budgetRequestService.getBudgetRequestsByClientId(clientId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BudgetRequestResponseDTO>> getBudgetRequestsByStatus(
            @PathVariable BudgetRequest.BudgetStatus status) {
        return ResponseEntity.ok(budgetRequestService.getBudgetRequestsByStatus(status));
    }

    @GetMapping("/location")
    public ResponseEntity<List<BudgetRequestResponseDTO>> getBudgetRequestsByLocation(
            @RequestParam String city,
            @RequestParam String state) {
        return ResponseEntity.ok(budgetRequestService.getBudgetRequestsByLocation(city, state));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BudgetRequestResponseDTO> updateBudgetRequestStatus(
            @PathVariable Long id,
            @RequestParam BudgetRequest.BudgetStatus status) {
        return ResponseEntity.ok(budgetRequestService.updateBudgetRequestStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudgetRequest(@PathVariable Long id) {
        budgetRequestService.deleteBudgetRequest(id);
        return ResponseEntity.noContent().build();
    }
} 