package com.plataformamarcenaria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "budget_requests")
public class BudgetRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    @NotBlank
    private String description;

    @ElementCollection
    private List<String> referenceImages = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private BudgetStatus status = BudgetStatus.OPEN;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address location;

    private BigDecimal estimatedBudget;

    private LocalDateTime desiredDeadline;

    @OneToMany(mappedBy = "budgetRequest", cascade = CascadeType.ALL)
    private List<Visit> visits = new ArrayList<>();

    @OneToMany(mappedBy = "budgetRequest", cascade = CascadeType.ALL)
    private List<Bid> bids = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum BudgetStatus {
        OPEN,
        WAITING_VISIT,
        WAITING_BIDS,
        CLOSED,
        CANCELLED
    }
} 