package com.api.marketplace.services;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.CourseAdminDTO;
import com.api.marketplace.dtos.CourseRequestDTO;
import com.api.marketplace.dtos.CourseResponseDTO;
import com.api.marketplace.dtos.CourseResponseLiteDTO;

/**
 * Interfaz de servicio para la gestión de cursos.
 * Define los métodos que deben ser implementados para manejar las operaciones
 * relacionadas con la entidad Course.
 */
public interface CourseService {

    /**
     * Obtiene una lista paginada de todos los cursos publicados (visibles
     * públicamente),
     * con posibilidad de aplicar filtros por título y categoría.
     *
     * @param page     Número de página a recuperar
     * @param size     Número de elementos por página
     * @param title    Título del curso (opcional)
     * @param category Categoría del curso (opcional)
     * @return Página con objetos de tipo CourseResponseLiteDTO
     */
    Page<CourseResponseLiteDTO> getAllPublishedCoursesPaginated(int page, int size, String title, String category);

    /**
     * Obtiene todos los cursos creados por el usuario autenticado, con soporte para
     * paginación y filtros por título y categoría.
     *
     * @param page              Número de página a recuperar
     * @param size              Número de elementos por página
     * @param title             Título del curso (opcional)
     * @param category          Categoría del curso (opcional)
     * @param userAuthenticated Usuario autenticado propietario de los cursos
     * @return Página con objetos de tipo CourseAdminDTO
     */
    Page<CourseAdminDTO> getAllAuthenticatedUserCourses(int page, int size, String title, String category,
            User userAuthenticated);

    /**
     * Obtiene una lista paginada de cursos populares (todos los cursos publicados).
     *
     * @param page Número de página a recuperar
     * @param size Número de elementos por página
     * @return Página con objetos de tipo CourseResponseLiteDTO
     */
    Page<CourseResponseLiteDTO> getPopularCourses(int page, int size);

    /**
     * Recupera un curso a partir de su ID (clave primaria).
     *
     * @param id ID numérico del curso
     * @return DTO con la información del curso
     */
    CourseResponseDTO getCourseById(int id);

    /**
     * Recupera un curso a partir de su UUID (clave única).
     *
     * @param uuid UUID del curso
     * @return DTO con la información del curso
     */
    CourseResponseDTO getCourseByUuid(UUID uuid);

    /**
     * Crea un nuevo curso con los datos proporcionados.
     *
     * @param dto        DTO con los datos del curso
     * @param instructor Usuario que actúa como instructor/creador del curso
     * @return DTO con la información del curso creado
     */
    CourseResponseDTO createCourse(CourseRequestDTO dto, User instructor);

    /**
     * Actualiza un curso existente, verificando que el usuario autenticado sea el
     * propietario.
     *
     * @param uuid              UUID del curso a actualizar
     * @param dto               DTO con los datos nuevos
     * @param authenticatedUser Usuario autenticado (debe ser el propietario del
     *                          curso)
     * @return DTO actualizado del curso
     */
    CourseResponseDTO updateCourse(UUID uuid, CourseRequestDTO dto, User authenticatedUser);

    /**
     * Elimina un curso existente, siempre y cuando pertenezca al usuario
     * autenticado.
     *
     * @param uuid              UUID del curso a eliminar
     * @param authenticatedUser Usuario autenticado (debe ser el propietario del
     *                          curso)
     */
    void deleteCourse(UUID uuid, User authenticatedUser);
}
