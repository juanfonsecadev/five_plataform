package com.plataformamarcenaria.dto.address;

import com.plataformamarcenaria.dto.user.UserResponseDTO;
import lombok.Data;

@Data
public class AddressResponseDTO {
    private Long id;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
    private String reference;
    private UserResponseDTO user;

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder()
            .append(street)
            .append(", ")
            .append(number);
        
        if (complement != null && !complement.isEmpty()) {
            sb.append(" - ").append(complement);
        }
        
        sb.append(", ")
            .append(neighborhood)
            .append(", ")
            .append(city)
            .append(" - ")
            .append(state)
            .append(", ")
            .append(zipCode);

        return sb.toString();
    }
} 