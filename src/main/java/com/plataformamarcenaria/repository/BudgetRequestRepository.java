package com.plataformamarcenaria.repository;

import com.plataformamarcenaria.entity.BudgetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRequestRepository extends JpaRepository<BudgetRequest, Long> {
    List<BudgetRequest> findByClientId(Long clientId);
    List<BudgetRequest> findByStatus(BudgetRequest.BudgetStatus status);
    List<BudgetRequest> findByLocationCityAndLocationState(String city, String state);
    List<BudgetRequest> findByStatusAndLocationCityAndLocationState(
        BudgetRequest.BudgetStatus status, 
        String city, 
        String state
    );
} 