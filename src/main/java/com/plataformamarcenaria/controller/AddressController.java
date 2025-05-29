package com.plataformamarcenaria.controller;

import com.plataformamarcenaria.dto.address.AddressCreateDTO;
import com.plataformamarcenaria.dto.address.AddressResponseDTO;
import com.plataformamarcenaria.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponseDTO> createAddress(@Valid @RequestBody AddressCreateDTO createDTO) {
        return new ResponseEntity<>(addressService.createAddress(createDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> getAddressById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getAddressById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressResponseDTO>> getAddressesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getAddressesByUserId(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<AddressResponseDTO>> getAddressesByCityAndState(
            @RequestParam String city,
            @RequestParam String state) {
        return ResponseEntity.ok(addressService.getAddressesByCityAndState(city, state));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
} 