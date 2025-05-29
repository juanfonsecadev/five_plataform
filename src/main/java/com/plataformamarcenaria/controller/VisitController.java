package com.plataformamarcenaria.controller;

import com.plataformamarcenaria.dto.visit.VisitCreateDTO;
import com.plataformamarcenaria.dto.visit.VisitResponseDTO;
import com.plataformamarcenaria.entity.Visit;
import com.plataformamarcenaria.service.VisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;

    @PostMapping
    public ResponseEntity<VisitResponseDTO> createVisit(@Valid @RequestBody VisitCreateDTO createDTO) {
        return new ResponseEntity<>(visitService.createVisit(createDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitResponseDTO> getVisitById(@PathVariable Long id) {
        return ResponseEntity.ok(visitService.getVisitById(id));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<VisitResponseDTO>> getVisitsBySellerId(@PathVariable Long sellerId) {
        return ResponseEntity.ok(visitService.getVisitsBySellerId(sellerId));
    }

    @GetMapping("/budget-request/{budgetRequestId}")
    public ResponseEntity<List<VisitResponseDTO>> getVisitsByBudgetRequestId(
            @PathVariable Long budgetRequestId) {
        return ResponseEntity.ok(visitService.getVisitsByBudgetRequestId(budgetRequestId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<VisitResponseDTO> updateVisitStatus(
            @PathVariable Long id,
            @RequestParam Visit.VisitStatus status) {
        return ResponseEntity.ok(visitService.updateVisitStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable Long id) {
        visitService.deleteVisit(id);
        return ResponseEntity.noContent().build();
    }
} 