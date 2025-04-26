package com.api.marketplace.dtos;

import com.api.marketplace.enums.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseRequestDTO {

    // Atributos
    private String title;
    private String description;
    private Category category;
    private float price;
    private String thumbnail_url;

}
