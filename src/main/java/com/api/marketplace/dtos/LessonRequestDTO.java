package com.api.marketplace.dtos;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LessonRequestDTO {

    // Atributos
    @NotBlank(message = "El título de la lección es obligatorio")
    @Size(max = 150, message = "El título no puede tener más de 150 caracteres")
    private String title;

    @NotBlank(message = "La URL del video es obligatoria")
    @Size(max = 500, message = "La URL del video no puede tener más de 500 caracteres")
    private String video_url;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 5000, message = "La descripción no puede tener más de 5000 caracteres")
    private String description;

    @NotBlank(message = "La URL de la miniatura es obligatoria")
    @Size(max = 300, message = "La URL de la miniatura no puede tener más de 300 caracteres")
    private String thumbnail_url;

    @Positive(message = "La duración debe ser mayor que 0")
    private int durationMinutes;

    private boolean freePreview;

    @NotNull(message = "El ID del curso es obligatorio")
    private UUID idCourse;

    @Min(value = 1, message = "La posición debe ser mayor o igual a 1")
    private int position;
}
