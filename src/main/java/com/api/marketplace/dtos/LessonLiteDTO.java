package com.api.marketplace.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LessonLiteDTO {

    // Atributos
    private int id;
    private String title;
    private int position;
    private String thumbnail_url;
}
