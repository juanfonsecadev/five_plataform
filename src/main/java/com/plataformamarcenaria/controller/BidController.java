package com.plataformamarcenaria.controller;

import com.plataformamarcenaria.dto.bid.BidCreateDTO;
import com.plataformamarcenaria.dto.bid.BidResponseDTO;
import com.plataformamarcenaria.service.BidService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
public class BidController {
    private final BidService bidService;

    @PostMapping
    public ResponseEntity<BidResponseDTO> createBid(@Valid @RequestBody BidCreateDTO createDTO) {
        return new ResponseEntity<>(bidService.createBid(createDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BidResponseDTO> getBidById(@PathVariable Long id) {
        return ResponseEntity.ok(bidService.getBidById(id));
    }

    @GetMapping("/carpenter/{carpenterId}")
    public ResponseEntity<List<BidResponseDTO>> getBidsByCarpenterId(@PathVariable Long carpenterId) {
        return ResponseEntity.ok(bidService.getBidsByCarpenterId(carpenterId));
    }

    @GetMapping("/budget-request/{budgetRequestId}")
    public ResponseEntity<List<BidResponseDTO>> getBidsByBudgetRequestId(
            @PathVariable Long budgetRequestId) {
        return ResponseEntity.ok(bidService.getBidsByBudgetRequestId(budgetRequestId));
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<BidResponseDTO> acceptBid(@PathVariable Long id) {
        return ResponseEntity.ok(bidService.acceptBid(id));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<BidResponseDTO> rejectBid(@PathVariable Long id) {
        return ResponseEntity.ok(bidService.rejectBid(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBid(@PathVariable Long id) {
        bidService.deleteBid(id);
        return ResponseEntity.noContent().build();
    }
} 