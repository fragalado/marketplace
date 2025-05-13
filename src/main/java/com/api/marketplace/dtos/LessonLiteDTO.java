package com.api.marketplace.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LessonLiteDTO {

    // Atributos
    private String uuid;
    private String title;
    private int position;
    private String thumbnail_url;
    private boolean freePreview;
}
