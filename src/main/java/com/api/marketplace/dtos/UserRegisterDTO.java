package com.api.marketplace.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterDTO {

    // Atributos
    private String username;
    private String email;
    private String password;
}
