package com.api.marketplace.services;

import java.util.List;

import com.api.marketplace.dtos.LessonRequestDTO;
import com.api.marketplace.dtos.LessonResponseDTO;

/**
 * Interfaz que define los metodos que daran servicio a Lesson
 */
public interface LessonService {

    /**
     * Metodo que devuelve una lista con todos los lessons
     * 
     * @return List<LessonResponseDTO> lista con todos los lessons
     */
    List<LessonResponseDTO> getAllLessons();

    /**
     * Metodo que devuelve un lesson por su id
     * 
     * @param idLesson Id del lesson a obtener
     * @return LessonResponseDTO
     */
    LessonResponseDTO getLessonById(int idLesson);

    /**
     * Metodo que crea un nuevo lesson
     * 
     * @param dto Objeto con los datos del nuevo lesson ha crear
     * @return LessonResponseDTO objeto con los datos del nuevo lesson
     */
    LessonResponseDTO createLesson(LessonRequestDTO dto);

    /**
     * Metodo que actualia un lesson
     * 
     * @param idLesson Id del lesson a actualizar
     * @param dto      LessonRequestDTO objeto con los nuevos datos del lesson
     * @return LessonResponseDTO objeto con los nuevos datos del lesson
     */
    LessonResponseDTO updateLesson(int idLesson, LessonRequestDTO dto);

    /**
     * Metodo que elimina un lesson por su id
     * 
     * @param idLesson Id del lesson a eliminar
     */
    void deleteLesson(int idLesson);
}
