package com.plataformamarcenaria.service;

import com.plataformamarcenaria.dto.bid.BidCreateDTO;
import com.plataformamarcenaria.dto.bid.BidResponseDTO;
import com.plataformamarcenaria.entity.Bid;
import com.plataformamarcenaria.entity.BudgetRequest;
import com.plataformamarcenaria.entity.User;
import com.plataformamarcenaria.exception.BusinessException;
import com.plataformamarcenaria.exception.ResourceNotFoundException;
import com.plataformamarcenaria.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final UserService userService;
    private final BudgetRequestService budgetRequestService;

    @Transactional
    public BidResponseDTO createBid(BidCreateDTO createDTO) {
        User carpenter = userService.findUserById(createDTO.getCarpenterId());
        if (carpenter.getUserType() != User.UserType.CARPENTER) {
            throw new BusinessException("Apenas marceneiros podem enviar lances");
        }

        BudgetRequest budgetRequest = budgetRequestService.findBudgetRequestById(createDTO.getBudgetRequestId());
        if (budgetRequest.getStatus() != BudgetRequest.BudgetStatus.WAITING_BIDS) {
            throw new BusinessException("Este orçamento não está aceitando lances no momento");
        }

        // Verificar se o marceneiro já deu um lance para este orçamento
        if (hasExistingBid(carpenter.getId(), budgetRequest.getId())) {
            throw new BusinessException("Você já enviou um lance para este orçamento");
        }

        Bid bid = new Bid();
        bid.setCarpenter(carpenter);
        bid.setBudgetRequest(budgetRequest);
        bid.setPrice(createDTO.getPrice());
        bid.setExecutionTimeInDays(createDTO.getExecutionTimeInDays());
        bid.setDescription(createDTO.getDescription());
        bid.setStatus(Bid.BidStatus.PENDING);

        return convertToResponseDTO(bidRepository.save(bid));
    }

    @Transactional(readOnly = true)
    public BidResponseDTO getBidById(Long id) {
        return convertToResponseDTO(findBidById(id));
    }

    @Transactional(readOnly = true)
    public List<BidResponseDTO> getBidsByCarpenterId(Long carpenterId) {
        return bidRepository.findByCarpenterId(carpenterId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BidResponseDTO> getBidsByBudgetRequestId(Long budgetRequestId) {
        return bidRepository.findByBudgetRequestId(budgetRequestId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BidResponseDTO acceptBid(Long id) {
        Bid bid = findBidById(id);
        validateBidAcceptance(bid);

        bid.setStatus(Bid.BidStatus.ACCEPTED);
        Bid acceptedBid = bidRepository.save(bid);

        // Rejeitar outros lances
        rejectOtherBids(bid.getBudgetRequest().getId(), bid.getId());

        // Atualizar status do orçamento
        budgetRequestService.updateBudgetRequestStatus(
            bid.getBudgetRequest().getId(),
            BudgetRequest.BudgetStatus.CLOSED
        );

        return convertToResponseDTO(acceptedBid);
    }

    @Transactional
    public BidResponseDTO rejectBid(Long id) {
        Bid bid = findBidById(id);
        validateBidRejection(bid);

        bid.setStatus(Bid.BidStatus.REJECTED);
        return convertToResponseDTO(bidRepository.save(bid));
    }

    @Transactional
    public void deleteBid(Long id) {
        Bid bid = findBidById(id);
        if (bid.getStatus() != Bid.BidStatus.PENDING) {
            throw new BusinessException("Não é possível excluir um lance que já foi aceito ou rejeitado");
        }
        bidRepository.delete(bid);
    }

    private boolean hasExistingBid(Long carpenterId, Long budgetRequestId) {
        return !bidRepository.findByBudgetRequestIdAndStatus(budgetRequestId, Bid.BidStatus.PENDING)
                .stream()
                .filter(bid -> bid.getCarpenter().getId().equals(carpenterId))
                .collect(Collectors.toList())
                .isEmpty();
    }

    private void validateBidAcceptance(Bid bid) {
        if (bid.getStatus() != Bid.BidStatus.PENDING) {
            throw new BusinessException("Este lance não está mais pendente");
        }

        if (bid.getBudgetRequest().getStatus() != BudgetRequest.BudgetStatus.WAITING_BIDS) {
            throw new BusinessException("Este orçamento não está mais aceitando lances");
        }
    }

    private void validateBidRejection(Bid bid) {
        if (bid.getStatus() != Bid.BidStatus.PENDING) {
            throw new BusinessException("Este lance não está mais pendente");
        }
    }

    private void rejectOtherBids(Long budgetRequestId, Long acceptedBidId) {
        bidRepository.findByBudgetRequestIdAndStatus(budgetRequestId, Bid.BidStatus.PENDING)
                .stream()
                .filter(bid -> !bid.getId().equals(acceptedBidId))
                .forEach(bid -> {
                    bid.setStatus(Bid.BidStatus.REJECTED);
                    bidRepository.save(bid);
                });
    }

    private Bid findBidById(Long id) {
        return bidRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lance", "id", id));
    }

    private BidResponseDTO convertToResponseDTO(Bid bid) {
        BidResponseDTO dto = new BidResponseDTO();
        dto.setId(bid.getId());
        dto.setCarpenter(userService.getUserById(bid.getCarpenter().getId()));
        dto.setBudgetRequest(budgetRequestService.getBudgetRequestById(bid.getBudgetRequest().getId()));
        dto.setPrice(bid.getPrice());
        dto.setExecutionTimeInDays(bid.getExecutionTimeInDays());
        dto.setDescription(bid.getDescription());
        dto.setStatus(bid.getStatus());
        dto.setCreatedAt(bid.getCreatedAt());
        dto.setUpdatedAt(bid.getUpdatedAt());
        return dto;
    }
} 