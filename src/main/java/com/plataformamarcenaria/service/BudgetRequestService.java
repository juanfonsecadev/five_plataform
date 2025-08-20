package com.plataformamarcenaria.service;

import com.plataformamarcenaria.dto.budget.BudgetRequestCreateDTO;
import com.plataformamarcenaria.dto.budget.BudgetRequestResponseDTO;
import com.plataformamarcenaria.entity.Address;
import com.plataformamarcenaria.entity.Bid;
import com.plataformamarcenaria.entity.BudgetRequest;
import com.plataformamarcenaria.entity.User;
import com.plataformamarcenaria.exception.BusinessException;
import com.plataformamarcenaria.exception.ResourceNotFoundException;
import com.plataformamarcenaria.repository.BudgetRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetRequestService {
    private final BudgetRequestRepository budgetRequestRepository;
    private final UserService userService;
    private final AddressService addressService;

    @Transactional
    public BudgetRequestResponseDTO createBudgetRequest(BudgetRequestCreateDTO createDTO) {
        User client = userService.findUserById(createDTO.getClientId());
        if (client.getUserType() != User.UserType.CLIENT) {
            throw new BusinessException("Apenas clientes podem criar orçamentos");
        }

        Address location = addressService.findAddressById(createDTO.getLocationId());
        if (!location.getUser().getId().equals(client.getId())) {
            throw new BusinessException("O endereço informado não pertence ao cliente");
        }

        BudgetRequest budgetRequest = new BudgetRequest();
        budgetRequest.setClient(client);
        budgetRequest.setDescription(createDTO.getDescription());
        budgetRequest.setReferenceImages(createDTO.getReferenceImages());
        budgetRequest.setLocation(location);
        budgetRequest.setEstimatedBudget(createDTO.getEstimatedBudget());
        budgetRequest.setDesiredDeadline(createDTO.getDesiredDeadline());
        budgetRequest.setStatus(BudgetRequest.BudgetStatus.OPEN);

        return convertToResponseDTO(budgetRequestRepository.save(budgetRequest));
    }

    @Transactional(readOnly = true)
    public BudgetRequestResponseDTO getBudgetRequestById(Long id) {
        return convertToResponseDTO(findBudgetRequestById(id));
    }

    @Transactional(readOnly = true)
    public List<BudgetRequestResponseDTO> getBudgetRequestsByClientId(Long clientId) {
        return budgetRequestRepository.findByClientId(clientId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BudgetRequestResponseDTO> getBudgetRequestsByStatus(BudgetRequest.BudgetStatus status) {
        return budgetRequestRepository.findByStatus(status).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BudgetRequestResponseDTO> getBudgetRequestsByLocation(String city, String state) {
        return budgetRequestRepository.findByLocationCityAndLocationState(city, state).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BudgetRequestResponseDTO updateBudgetRequestStatus(Long id, BudgetRequest.BudgetStatus newStatus) {
        BudgetRequest budgetRequest = findBudgetRequestById(id);
        validateStatusTransition(budgetRequest.getStatus(), newStatus);
        budgetRequest.setStatus(newStatus);
        return convertToResponseDTO(budgetRequestRepository.save(budgetRequest));
    }

    @Transactional
    public void deleteBudgetRequest(Long id) {
        BudgetRequest budgetRequest = findBudgetRequestById(id);
        if (budgetRequest.getStatus() != BudgetRequest.BudgetStatus.OPEN) {
            throw new BusinessException("Não é possível excluir um orçamento que já está em andamento");
        }
        budgetRequestRepository.delete(budgetRequest);
    }

    private void validateStatusTransition(BudgetRequest.BudgetStatus currentStatus, BudgetRequest.BudgetStatus newStatus) {
        if (currentStatus == BudgetRequest.BudgetStatus.CLOSED || currentStatus == BudgetRequest.BudgetStatus.CANCELLED) {
            throw new BusinessException("Não é possível alterar o status de um orçamento finalizado ou cancelado");
        }

        if (newStatus == BudgetRequest.BudgetStatus.OPEN && currentStatus != BudgetRequest.BudgetStatus.OPEN) {
            throw new BusinessException("Não é possível voltar o status para aberto");
        }
    }

    public BudgetRequest findBudgetRequestById(Long id) {
        return budgetRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orçamento", "id", id));
    }

    private BudgetRequestResponseDTO convertToResponseDTO(BudgetRequest budgetRequest) {
        BudgetRequestResponseDTO dto = new BudgetRequestResponseDTO();
        dto.setId(budgetRequest.getId());
        dto.setClient(userService.getUserById(budgetRequest.getClient().getId()));
        dto.setDescription(budgetRequest.getDescription());
        dto.setReferenceImages(budgetRequest.getReferenceImages());
        dto.setStatus(budgetRequest.getStatus());
        dto.setLocation(addressService.getAddressById(budgetRequest.getLocation().getId()));
        dto.setEstimatedBudget(budgetRequest.getEstimatedBudget());
        dto.setDesiredDeadline(budgetRequest.getDesiredDeadline());
        dto.setCreatedAt(budgetRequest.getCreatedAt());
        dto.setUpdatedAt(budgetRequest.getUpdatedAt());

        // Calculando estatísticas dos lances
        List<Bid> bids = budgetRequest.getBids();
        dto.setTotalBids(bids.size());
        if (!bids.isEmpty()) {
            dto.setLowestBid(bids.stream()
                    .map(Bid::getPrice)
                    .min(BigDecimal::compareTo)
                    .orElse(null));
            dto.setHighestBid(bids.stream()
                    .map(Bid::getPrice)
                    .max(BigDecimal::compareTo)
                    .orElse(null));
        }

        return dto;
    }
} 