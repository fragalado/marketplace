package com.api.marketplace.dtos;

import java.time.LocalDateTime;

import com.api.marketplace.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    // Atributos
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePicture;
    private String email;
    private Role role;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
