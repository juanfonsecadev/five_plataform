package com.plataformamarcenaria.dto.user;

import com.plataformamarcenaria.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private User.UserType userType;
    private String avatar;
    private String document;
    private boolean active;
    private double rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 