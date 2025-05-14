package com.api.marketplace.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LessonRequestDTO {

    // Atributos
    private String title;
    private String video_url;
    private String description;
    private String thumbnail_url;
    private int durationMinutes;
    private boolean freePreview;
    private String idCourse;
}
