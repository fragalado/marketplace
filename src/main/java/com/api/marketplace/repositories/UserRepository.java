package com.api.marketplace.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.marketplace.daos.User;

/**
 * Repositorio JPA para la entidad User.
 * Proporciona métodos de consulta personalizados para la gestión de usuarios.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Busca un usuario por su email.
     *
     * @param email Email del usuario
     * @return Optional que puede contener el usuario si existe
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca un usuario por su nombre de usuario (username).
     *
     * @param username Nombre de usuario
     * @return Optional que puede contener el usuario si existe
     */
    Optional<User> findByUsername(String username);

    /**
     * Busca un usuario por email o username (útil para login flexible).
     *
     * @param email    Email del usuario
     * @param username Nombre de usuario
     * @return Optional que puede contener el usuario si existe
     */
    Optional<User> findByEmailOrUsername(String email, String username);

    /**
     * Verifica si ya existe un usuario con el email proporcionado.
     *
     * @param email Email a verificar
     * @return true si el email ya está registrado, false en caso contrario
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si ya existe un usuario con el username proporcionado.
     *
     * @param username Nombre de usuario a verificar
     * @return true si el username ya está registrado, false en caso contrario
     */
    boolean existsByUsername(String username);
}
