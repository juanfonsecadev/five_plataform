package com.plataformamarcenaria.service;

import com.plataformamarcenaria.dto.user.UserCreateDTO;
import com.plataformamarcenaria.dto.user.UserResponseDTO;
import com.plataformamarcenaria.dto.user.UserUpdateDTO;
import com.plataformamarcenaria.entity.User;
import com.plataformamarcenaria.exception.BusinessException;
import com.plataformamarcenaria.exception.ResourceNotFoundException;
import com.plataformamarcenaria.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO createUser(UserCreateDTO createDTO) {
        if (userRepository.existsByEmail(createDTO.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }

        User user = new User();
        user.setName(createDTO.getName());
        user.setEmail(createDTO.getEmail());
        user.setPhone(createDTO.getPhone());
        user.setPassword(passwordEncoder.encode(createDTO.getPassword()));
        user.setUserType(createDTO.getUserType());
        user.setDocument(createDTO.getDocument());
        user.setActive(true);
        user.setRating(0.0);

        return convertToResponseDTO(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        return convertToResponseDTO(findUserById(id));
    }

    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "id", id));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByType(User.UserType userType) {
        return userRepository.findByUserType(userType).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateDTO updateDTO) {
        User user = findUserById(id);

        if (updateDTO.getEmail() != null && !updateDTO.getEmail().equals(user.getEmail()) &&
            userRepository.existsByEmail(updateDTO.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }

        if (updateDTO.getName() != null) {
            user.setName(updateDTO.getName());
        }
        if (updateDTO.getEmail() != null) {
            user.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getPhone() != null) {
            user.setPhone(updateDTO.getPhone());
        }
        if (updateDTO.getAvatar() != null) {
            user.setAvatar(updateDTO.getAvatar());
        }
        if (updateDTO.getDocument() != null) {
            user.setDocument(updateDTO.getDocument());
        }
        if (updateDTO.getActive() != null) {
            user.setActive(updateDTO.getActive());
        }

        return convertToResponseDTO(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findUserById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setUserType(user.getUserType());
        dto.setAvatar(user.getAvatar());
        dto.setDocument(user.getDocument());
        dto.setActive(user.isActive());
        dto.setRating(user.getRating());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
} 