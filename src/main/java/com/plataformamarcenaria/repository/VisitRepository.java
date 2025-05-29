package com.plataformamarcenaria.repository;

import com.plataformamarcenaria.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findBySellerId(Long sellerId);
    List<Visit> findByBudgetRequestId(Long budgetRequestId);
    List<Visit> findByStatus(Visit.VisitStatus status);
    List<Visit> findByScheduledDateBetween(LocalDateTime start, LocalDateTime end);
    List<Visit> findBySellerIdAndScheduledDateBetween(
        Long sellerId, 
        LocalDateTime start, 
        LocalDateTime end
    );
} 