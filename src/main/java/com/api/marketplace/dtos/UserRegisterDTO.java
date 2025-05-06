package com.api.marketplace.dtos;

import com.api.marketplace.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterDTO {

    // Atributos
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}
