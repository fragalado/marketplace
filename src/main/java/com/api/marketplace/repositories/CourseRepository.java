package com.api.marketplace.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.marketplace.daos.Course;
import com.api.marketplace.daos.User;
import com.api.marketplace.enums.Category;

/**
 * Repositorio JPA para la entidad Course.
 * Proporciona métodos para acceder a los cursos desde la base de datos,
 * con soporte para paginación y filtrado.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

        /**
         * Devuelve una página de cursos que están publicados.
         *
         * @param pageable Objeto que contiene la información de paginación
         * @return Página de cursos publicados
         */
        Page<Course> findByPublishedTrue(Pageable pageable);

        /**
         * Devuelve una página de cursos creados por un usuario específico.
         *
         * @param user     Usuario autenticado (instructor)
         * @param pageable Parámetros de paginación
         * @return Página de cursos pertenecientes al usuario
         */
        Page<Course> findByUser(User user, Pageable pageable);

        /**
         * Devuelve una página de cursos de un usuario que coincidan parcialmente con un
         * título.
         *
         * @param user     Usuario autenticado
         * @param title    Título parcial a buscar (insensible a mayúsculas)
         * @param pageable Parámetros de paginación
         * @return Página de cursos filtrados por título
         */
        Page<Course> findByUserAndTitleContainingIgnoreCase(User user, String title, Pageable pageable);

        /**
         * Devuelve una página de cursos de un usuario filtrados por categoría.
         *
         * @param user     Usuario autenticado
         * @param category Categoría del curso
         * @param pageable Parámetros de paginación
         * @return Página de cursos filtrados por categoría
         */
        Page<Course> findByUserAndCategory(User user, Category category, Pageable pageable);

        /**
         * Devuelve una página de cursos de un usuario filtrados por título y categoría.
         *
         * @param user     Usuario autenticado
         * @param title    Título parcial
         * @param category Categoría del curso
         * @param pageable Parámetros de paginación
         * @return Página de cursos filtrados por título y categoría
         */
        Page<Course> findByUserAndTitleContainingIgnoreCaseAndCategory(User user, String title, Category category,
                        Pageable pageable);

        /**
         * Devuelve una página de cursos publicados cuyo título coincida parcialmente.
         *
         * @param title    Título parcial (insensible a mayúsculas)
         * @param pageable Parámetros de paginación
         * @return Página de cursos publicados filtrados por título
         */
        Page<Course> findByPublishedTrueAndTitleContainingIgnoreCase(String title, Pageable pageable);

        /**
         * Devuelve una página de cursos publicados filtrados por categoría.
         *
         * @param category Categoría del curso
         * @param pageable Parámetros de paginación
         * @return Página de cursos publicados de una categoría
         */
        Page<Course> findByPublishedTrueAndCategory(Category category, Pageable pageable);

        /**
         * Devuelve una página de cursos publicados filtrados por título y categoría.
         *
         * @param title    Título parcial
         * @param category Categoría del curso
         * @param pageable Parámetros de paginación
         * @return Página de cursos publicados filtrados por título y categoría
         */
        Page<Course> findByPublishedTrueAndTitleContainingIgnoreCaseAndCategory(String title, Category category,
                        Pageable pageable);

        /**
         * Busca un curso por su UUID.
         *
         * @param uuid Identificador único del curso
         * @return Curso encontrado, si existe
         */
        Optional<Course> findByUuid(UUID uuid);
}
