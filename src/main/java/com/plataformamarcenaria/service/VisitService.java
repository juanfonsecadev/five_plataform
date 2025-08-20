package com.plataformamarcenaria.service;

import com.plataformamarcenaria.dto.visit.VisitCreateDTO;
import com.plataformamarcenaria.dto.visit.VisitResponseDTO;
import com.plataformamarcenaria.entity.BudgetRequest;
import com.plataformamarcenaria.entity.User;
import com.plataformamarcenaria.entity.Visit;
import com.plataformamarcenaria.exception.BusinessException;
import com.plataformamarcenaria.exception.ResourceNotFoundException;
import com.plataformamarcenaria.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final UserService userService;
    private final BudgetRequestService budgetRequestService;

    @Transactional
    public VisitResponseDTO createVisit(VisitCreateDTO createDTO) {
        User seller = userService.findUserById(createDTO.getSellerId());
        if (seller.getUserType() != User.UserType.SELLER) {
            throw new BusinessException("Apenas vendedores podem agendar visitas");
        }

        BudgetRequest budgetRequest = budgetRequestService.findBudgetRequestById(createDTO.getBudgetRequestId());
        if (budgetRequest.getStatus() != BudgetRequest.BudgetStatus.OPEN) {
            throw new BusinessException("Não é possível agendar visita para um orçamento que não está aberto");
        }

        // Verificar se já existe visita agendada para o mesmo horário
        if (hasConflictingVisit(seller.getId(), createDTO.getScheduledDate())) {
            throw new BusinessException("Já existe uma visita agendada para este horário");
        }

        Visit visit = new Visit();
        visit.setSeller(seller);
        visit.setBudgetRequest(budgetRequest);
        visit.setScheduledDate(createDTO.getScheduledDate());
        visit.setNotes(createDTO.getNotes());
        visit.setStatus(Visit.VisitStatus.SCHEDULED);

        Visit savedVisit = visitRepository.save(visit);

        // Atualizar status do orçamento
        budgetRequestService.updateBudgetRequestStatus(
            budgetRequest.getId(), 
            BudgetRequest.BudgetStatus.WAITING_VISIT
        );

        return convertToResponseDTO(savedVisit);
    }

    @Transactional(readOnly = true)
    public VisitResponseDTO getVisitById(Long id) {
        return convertToResponseDTO(findVisitById(id));
    }

    @Transactional(readOnly = true)
    public List<VisitResponseDTO> getVisitsBySellerId(Long sellerId) {
        return visitRepository.findBySellerId(sellerId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VisitResponseDTO> getVisitsByBudgetRequestId(Long budgetRequestId) {
        return visitRepository.findByBudgetRequestId(budgetRequestId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public VisitResponseDTO updateVisitStatus(Long id, Visit.VisitStatus newStatus) {
        Visit visit = findVisitById(id);
        validateStatusTransition(visit.getStatus(), newStatus);
        visit.setStatus(newStatus);

        if (newStatus == Visit.VisitStatus.COMPLETED) {
            budgetRequestService.updateBudgetRequestStatus(
                visit.getBudgetRequest().getId(),
                BudgetRequest.BudgetStatus.WAITING_BIDS
            );
        } else if (newStatus == Visit.VisitStatus.CANCELLED) {
            budgetRequestService.updateBudgetRequestStatus(
                visit.getBudgetRequest().getId(),
                BudgetRequest.BudgetStatus.OPEN
            );
        }

        return convertToResponseDTO(visitRepository.save(visit));
    }

    @Transactional
    public void deleteVisit(Long id) {
        Visit visit = findVisitById(id);
        if (visit.getStatus() != Visit.VisitStatus.SCHEDULED) {
            throw new BusinessException("Não é possível excluir uma visita que já foi realizada ou cancelada");
        }
        visitRepository.delete(visit);
    }

    private boolean hasConflictingVisit(Long sellerId, LocalDateTime scheduledDate) {
        LocalDateTime startWindow = scheduledDate.minusHours(2);
        LocalDateTime endWindow = scheduledDate.plusHours(2);

        return !visitRepository.findBySellerIdAndScheduledDateBetween(
            sellerId, 
            startWindow, 
            endWindow
        ).isEmpty();
    }

    private void validateStatusTransition(Visit.VisitStatus currentStatus, Visit.VisitStatus newStatus) {
        if (currentStatus == Visit.VisitStatus.COMPLETED || currentStatus == Visit.VisitStatus.CANCELLED) {
            throw new BusinessException("Não é possível alterar o status de uma visita finalizada ou cancelada");
        }

        if (newStatus == Visit.VisitStatus.SCHEDULED && currentStatus != Visit.VisitStatus.SCHEDULED) {
            throw new BusinessException("Não é possível voltar o status para agendado");
        }
    }

    private Visit findVisitById(Long id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visita", "id", id));
    }

    private VisitResponseDTO convertToResponseDTO(Visit visit) {
        VisitResponseDTO dto = new VisitResponseDTO();
        dto.setId(visit.getId());
        dto.setSeller(userService.getUserById(visit.getSeller().getId()));
        dto.setBudgetRequest(budgetRequestService.getBudgetRequestById(visit.getBudgetRequest().getId()));
        dto.setScheduledDate(visit.getScheduledDate());
        dto.setStatus(visit.getStatus());
        dto.setNotes(visit.getNotes());
        dto.setPhotos(visit.getPhotos());
        dto.setCreatedAt(visit.getCreatedAt());
        dto.setUpdatedAt(visit.getUpdatedAt());
        return dto;
    }
} 