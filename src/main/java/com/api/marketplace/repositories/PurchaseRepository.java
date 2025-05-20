package com.api.marketplace.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.marketplace.daos.Course;
import com.api.marketplace.daos.Purchase;
import com.api.marketplace.daos.User;

/**
 * Repositorio JPA para la entidad Purchase.
 * Permite realizar operaciones de persistencia relacionadas con compras de
 * cursos.
 */
@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    /**
     * Devuelve todas las compras realizadas por un usuario específico, paginadas.
     *
     * @param user     Usuario autenticado
     * @param pageable Parámetros de paginación
     * @return Página de objetos Purchase pertenecientes al usuario
     */
    Page<Purchase> findByUser(User user, Pageable pageable);

    /**
     * Verifica si un usuario ya ha comprado un curso en particular.
     *
     * @param user   Usuario autenticado
     * @param course Curso que se quiere verificar
     * @return true si el usuario ya compró el curso, false en caso contrario
     */
    boolean existsByUserAndCourse(User user, Course course);
}
