package com.api.marketplace.services;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.LessonRequestDTO;
import com.api.marketplace.dtos.LessonResponseDTO;

/**
 * Interfaz de servicio para la gestión de lecciones (lessons).
 * Define las operaciones disponibles para crear, consultar, actualizar y
 * eliminar lecciones.
 */
public interface LessonService {

    /**
     * Obtiene todas las lecciones asociadas a un curso, de forma paginada.
     * Solo el propietario del curso puede acceder a sus lecciones.
     *
     * @param uuidCourse        UUID del curso del que se quieren obtener las
     *                          lecciones
     * @param page              Número de página
     * @param size              Tamaño de cada página
     * @param authenticatedUser Usuario autenticado que realiza la petición
     * @return Página con objetos de tipo LessonResponseDTO
     */
    Page<LessonResponseDTO> getAllLessonsFromCourse(UUID uuidCourse, int page, int size, User authenticatedUser);

    /**
     * Obtiene una lección específica a partir de su UUID.
     *
     * @param uuidLesson UUID de la lección
     * @return DTO con la información de la lección
     */
    LessonResponseDTO getLessonByUuid(UUID uuidLesson);

    /**
     * Crea una nueva lección asociada a un curso.
     * Solo el instructor propietario del curso puede crear lecciones en él.
     *
     * @param dto               DTO con los datos de la nueva lección
     * @param authenticatedUser Usuario autenticado que realiza la petición
     * @return DTO con la información de la lección creada
     */
    LessonResponseDTO createLesson(LessonRequestDTO dto, User authenticatedUser);

    /**
     * Actualiza una lección existente.
     * Solo el instructor propietario del curso puede modificar sus lecciones.
     *
     * @param uuidLesson        UUID de la lección a actualizar
     * @param dto               DTO con los nuevos datos de la lección
     * @param authenticatedUser Usuario autenticado que realiza la petición
     * @return DTO con la información actualizada de la lección
     */
    LessonResponseDTO updateLesson(UUID uuidLesson, LessonRequestDTO dto, User authenticatedUser);

    /**
     * Elimina una lección específica.
     * Solo el instructor propietario del curso puede eliminar sus lecciones.
     *
     * @param uuidLesson        UUID de la lección a eliminar
     * @param authenticatedUser Usuario autenticado que realiza la petición
     */
    void deleteLesson(UUID uuidLesson, User authenticatedUser);
}
