package com.api.marketplace.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.marketplace.daos.Course;
import com.api.marketplace.daos.Lesson;

/**
 * Repositorio JPA para la entidad Lesson.
 * Proporciona acceso a las lecciones en la base de datos,
 * con soporte para paginación y búsqueda por UUID.
 */
@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    /**
     * Devuelve una página de lecciones asociadas a un curso específico.
     *
     * @param course   Curso al que pertenecen las lecciones
     * @param pageable Parámetros de paginación (página, tamaño, orden, etc.)
     * @return Página de lecciones del curso
     */
    Page<Lesson> findByCourse(Course course, Pageable pageable);

    /**
     * Busca una lección por su UUID.
     *
     * @param uuid Identificador único de la lección
     * @return Optional que puede contener la lección si se encuentra
     */
    Optional<Lesson> findByUuid(UUID uuid);
}
