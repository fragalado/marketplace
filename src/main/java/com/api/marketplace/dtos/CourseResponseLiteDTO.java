package com.api.marketplace.dtos;

import java.time.LocalDateTime;
import com.api.marketplace.enums.Category;
import com.api.marketplace.enums.Level;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponseLiteDTO {

    // Atributos
    private String uuid;
    private String title;
    private String description;
    private Category category;
    private float price;
    private String thumbnail_url; // URL de la imagen del curso
    private String language;
    private int durationMinutes;
    private Level level;
    private boolean published = false;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    private UserLiteDTO user;
}
