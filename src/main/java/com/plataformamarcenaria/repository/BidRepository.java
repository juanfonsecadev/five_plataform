package com.plataformamarcenaria.repository;

import com.plataformamarcenaria.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByCarpenterId(Long carpenterId);
    List<Bid> findByBudgetRequestId(Long budgetRequestId);
    List<Bid> findByStatus(Bid.BidStatus status);
    List<Bid> findByBudgetRequestIdAndStatus(Long budgetRequestId, Bid.BidStatus status);
    List<Bid> findByBudgetRequestIdOrderByPriceAsc(Long budgetRequestId);
} 