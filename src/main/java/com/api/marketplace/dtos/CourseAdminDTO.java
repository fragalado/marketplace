package com.api.marketplace.dtos;

import com.api.marketplace.enums.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseAdminDTO {

    // Atributos
    private String uuid;
    private String title;
    private String thumbnail_url;
    private Category category;
    private float price;
    private boolean published;
}
