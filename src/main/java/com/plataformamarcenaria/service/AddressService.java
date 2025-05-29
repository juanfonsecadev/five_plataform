package com.plataformamarcenaria.service;

import com.plataformamarcenaria.dto.address.AddressCreateDTO;
import com.plataformamarcenaria.dto.address.AddressResponseDTO;
import com.plataformamarcenaria.entity.Address;
import com.plataformamarcenaria.entity.User;
import com.plataformamarcenaria.exception.ResourceNotFoundException;
import com.plataformamarcenaria.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserService userService;

    @Transactional
    public AddressResponseDTO createAddress(AddressCreateDTO createDTO) {
        User user = userService.findUserById(createDTO.getUserId());

        Address address = new Address();
        address.setStreet(createDTO.getStreet());
        address.setNumber(createDTO.getNumber());
        address.setComplement(createDTO.getComplement());
        address.setNeighborhood(createDTO.getNeighborhood());
        address.setCity(createDTO.getCity());
        address.setState(createDTO.getState());
        address.setZipCode(createDTO.getZipCode());
        address.setReference(createDTO.getReference());
        address.setUser(user);

        return convertToResponseDTO(addressRepository.save(address));
    }

    @Transactional(readOnly = true)
    public AddressResponseDTO getAddressById(Long id) {
        return convertToResponseDTO(findAddressById(id));
    }

    @Transactional(readOnly = true)
    public List<AddressResponseDTO> getAddressesByUserId(Long userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AddressResponseDTO> getAddressesByCityAndState(String city, String state) {
        return addressRepository.findByCityAndState(city, state).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAddress(Long id) {
        Address address = findAddressById(id);
        addressRepository.delete(address);
    }

    @Transactional(readOnly = true)
    public Address findAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço", "id", id));
    }

    private AddressResponseDTO convertToResponseDTO(Address address) {
        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setNumber(address.getNumber());
        dto.setComplement(address.getComplement());
        dto.setNeighborhood(address.getNeighborhood());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setZipCode(address.getZipCode());
        dto.setReference(address.getReference());
        
        if (address.getUser() != null) {
            dto.setUser(userService.getUserById(address.getUser().getId()));
        }
        
        return dto;
    }
} 