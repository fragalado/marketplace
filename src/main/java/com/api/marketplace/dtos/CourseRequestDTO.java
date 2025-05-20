package com.api.marketplace.dtos;

import com.api.marketplace.enums.Category;
import com.api.marketplace.enums.Level;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseRequestDTO {

    // Atributos
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no puede tener más de 100 caracteres")
    private String title;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 5000, message = "La descripción no puede tener más de 5000 caracteres")
    private String description;

    @NotNull(message = "La categoría es obligatoria")
    private Category category;

    @PositiveOrZero(message = "El precio debe ser igual o mayor que 0")
    private float price;

    @NotBlank(message = "La URL de la imagen es obligatoria")
    @Size(max = 300, message = "La URL de la imagen no puede tener más de 300 caracteres")
    private String thumbnail_url;

    @NotBlank(message = "El idioma es obligatorio")
    private String language;

    @Positive(message = "La duración debe ser mayor que 0")
    private int durationMinutes;

    @NotNull(message = "El nivel es obligatorio")
    private Level level;

    private boolean published = false;

}
