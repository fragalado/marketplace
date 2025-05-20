package com.api.marketplace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.CourseAdminDTO;
import com.api.marketplace.dtos.CourseRequestDTO;
import com.api.marketplace.dtos.CourseResponseDTO;
import com.api.marketplace.dtos.CourseResponseLiteDTO;
import com.api.marketplace.services.CourseServiceImpl;

import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * Controlador REST que gestiona las operaciones relacionadas con los cursos.
 * Expone endpoints para obtener, crear, actualizar y eliminar cursos.
 */
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseServiceImpl courseService;

    public CourseController(CourseServiceImpl courseService) {
        this.courseService = courseService;
    }

    /**
     * Devuelve todos los cursos publicados de forma paginada. Se pueden aplicar
     * filtros por título y categoría.
     *
     * @param page     Número de página (por defecto 0)
     * @param size     Tamaño de la página (por defecto 10)
     * @param title    (Opcional) Filtro por título
     * @param category (Opcional) Filtro por categoría
     * @return Página de cursos publicados
     */
    @GetMapping("")
    public ResponseEntity<Page<CourseResponseLiteDTO>> getAllPublishedCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(courseService.getAllPublishedCoursesPaginated(page, size, title, category));
    }

    /**
     * Devuelve todos los cursos creados por el usuario autenticado, con soporte
     * para paginación y filtros.
     *
     * @param page           Número de página
     * @param size           Tamaño de página
     * @param title          (Opcional) Filtro por título
     * @param category       (Opcional) Filtro por categoría
     * @param authentication Objeto de autenticación para identificar al usuario
     * @return Página de cursos del usuario autenticado
     */
    @GetMapping("my-courses")
    public ResponseEntity<Page<CourseAdminDTO>> getAllAuthenticatedUserCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(courseService.getAllAuthenticatedUserCourses(page, size, title, category, user));
    }

    /**
     * Obtiene un curso por su UUID.
     *
     * @param uuid UUID del curso a obtener
     * @return Curso correspondiente al UUID proporcionado
     */
    @GetMapping("{uuid}")
    public ResponseEntity<CourseResponseDTO> getCourseByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(courseService.getCourseByUuid(uuid));
    }

    /**
     * Devuelve los cursos más populares (según el criterio definido en el
     * servicio), con paginación.
     *
     * @param page Número de página
     * @param size Tamaño de página (por defecto 6)
     * @return Página de cursos populares
     */
    @GetMapping("popular")
    public ResponseEntity<Page<CourseResponseLiteDTO>> getPopularCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(courseService.getPopularCourses(page, size));
    }

    /**
     * Crea un nuevo curso con los datos proporcionados.
     *
     * @param dto            Datos del nuevo curso
     * @param authentication Información del usuario autenticado
     * @return Curso creado
     */
    @PostMapping("")
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseRequestDTO dto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        CourseResponseDTO created = courseService.createCourse(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Actualiza un curso existente identificado por su UUID.
     *
     * @param uuid           UUID del curso
     * @param dto            Nuevos datos del curso
     * @param authentication Información del usuario autenticado
     * @return Curso actualizado
     */
    @PutMapping("{uuid}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable UUID uuid,
            @Valid @RequestBody CourseRequestDTO dto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(courseService.updateCourse(uuid, dto, user));
    }

    /**
     * Elimina un curso existente identificado por su UUID.
     *
     * @param uuid           UUID del curso
     * @param authentication Información del usuario autenticado
     * @return Respuesta sin contenido si la eliminación fue exitosa
     */
    @DeleteMapping("{uuid}")
    public ResponseEntity<Void> deleteCourse(@PathVariable UUID uuid, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        courseService.deleteCourse(uuid, user);
        return ResponseEntity.noContent().build();
    }

}
