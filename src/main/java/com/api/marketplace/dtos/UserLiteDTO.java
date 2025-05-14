package com.api.marketplace.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLiteDTO {

    // Atributos
    private String uuid;
    private String firstName;
    private String lastName;
}
