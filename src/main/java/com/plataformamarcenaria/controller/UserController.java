package com.plataformamarcenaria.controller;

import com.plataformamarcenaria.dto.user.UserCreateDTO;
import com.plataformamarcenaria.dto.user.UserResponseDTO;
import com.plataformamarcenaria.dto.user.UserUpdateDTO;
import com.plataformamarcenaria.entity.User;
import com.plataformamarcenaria.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO createDTO) {
        return new ResponseEntity<>(userService.createUser(createDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(
            @RequestParam(required = false) User.UserType userType) {
        if (userType != null) {
            return ResponseEntity.ok(userService.getUsersByType(userType));
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO updateDTO) {
        return ResponseEntity.ok(userService.updateUser(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
} 