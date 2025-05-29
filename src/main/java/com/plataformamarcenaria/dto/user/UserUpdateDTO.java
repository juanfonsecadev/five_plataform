package com.plataformamarcenaria.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserUpdateDTO {
    private String name;
    
    @Email(message = "Email inválido")
    private String email;
    
    private String phone;
    private String avatar;
    private String document;
    private Boolean active;
} 