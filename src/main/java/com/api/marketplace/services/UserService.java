package com.api.marketplace.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.CourseResponseLiteDTO;
import com.api.marketplace.dtos.UserDTO;
import com.api.marketplace.dtos.UserUpdateRequestDTO;

/**
 * Interfaz que define las operaciones relacionadas con los usuarios de la
 * plataforma.
 */
public interface UserService {

    /**
     * Actualiza la información del usuario autenticado.
     *
     * @param dto  Objeto que contiene los nuevos datos del usuario
     * @param user Objeto actual del usuario autenticado
     * @return Objeto UserDTO con la información actualizada
     */
    UserDTO updateUser(UserUpdateRequestDTO dto, User user);

    /**
     * Elimina un usuario por su ID.
     *
     * @param userId ID del usuario a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    boolean deleteUser(int userId);

    /**
     * Obtiene la lista de cursos comprados por un usuario autenticado de forma
     * paginada.
     *
     * @param page              Número de página
     * @param size              Tamaño de página
     * @param userAuthenticated Usuario autenticado
     * @return Página con los cursos comprados por el usuario
     */
    Page<CourseResponseLiteDTO> getPurchasedCourses(int page, int size, User userAuthenticated);

    /**
     * Obtiene la lista de UUIDs de los cursos comprados por un usuario.
     *
     * @param userAuthenticated Usuario autenticado
     * @return Lista de UUIDs de los cursos adquiridos
     */
    List<UUID> getUuidPurchasedCourses(User userAuthenticated);
}
