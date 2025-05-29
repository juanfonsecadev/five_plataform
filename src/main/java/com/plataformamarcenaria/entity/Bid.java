package com.plataformamarcenaria.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "bids")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carpenter_id")
    private User carpenter;

    @ManyToOne
    @JoinColumn(name = "budget_request_id")
    private BudgetRequest budgetRequest;

    private BigDecimal price;

    private Integer executionTimeInDays;

    private String description;

    @Enumerated(EnumType.STRING)
    private BidStatus status = BidStatus.PENDING;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum BidStatus {
        PENDING,
        ACCEPTED,
        REJECTED,
        CANCELLED
    }
} 