package com.api.marketplace.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LessonResponseDTO {

    // Atributos
    private int id;
    private String title;
    private String video_url;
    private int position;
    private String description;
    private String thumbnail_url;
    private int durationMinutes;
    private boolean freePreview;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    private CourseLiteDTO course;
}
