package com.api.marketplace.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.marketplace.daos.Course;
import com.api.marketplace.daos.User;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    /**
     * Metodo que devuelve todos los cursos publicados
     * 
     * @param pageable Objeto Pageable que contiene la pagina y el tamanyo de la
     *                 pagina
     * @return Devuelve una pagina con objetos de tipo curso
     */
    Page<Course> findByPublishedTrue(Pageable pageable);

    /**
     * Metodo que devuelve todos los cursos de un usuario
     * 
     * @param user     Usuario autenticado
     * @param pageable Objeto Pageable que contiene la pagina y el tamanyo de la
     *                 pagina
     * @return Devuelve una pagina con objetos de tipo curso
     */
    Page<Course> findByUser(User user, Pageable pageable);

    /**
     * 
     * @param uuid
     * @return
     */
    Optional<Course> findByUuid(UUID uuid);
}
