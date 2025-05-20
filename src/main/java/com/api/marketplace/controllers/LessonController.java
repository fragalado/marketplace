package com.api.marketplace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.LessonRequestDTO;
import com.api.marketplace.dtos.LessonResponseDTO;
import com.api.marketplace.services.LessonServiceImpl;

import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador REST que gestiona las operaciones relacionadas con las lecciones
 * de los cursos.
 */
@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonServiceImpl lessonService;

    public LessonController(LessonServiceImpl lessonServiceImpl) {
        this.lessonService = lessonServiceImpl;
    }

    /**
     * Obtiene una lección a partir de su UUID.
     *
     * @param uuid UUID de la lección
     * @return Objeto LessonResponseDTO correspondiente
     */
    @GetMapping("{uuid}")
    public ResponseEntity<LessonResponseDTO> getLessonByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(lessonService.getLessonByUuid(uuid));
    }

    /**
     * Devuelve todas las lecciones de un curso, paginadas.
     * Solo se permite si el usuario es el creador del curso.
     *
     * @param uuidCourse     UUID del curso
     * @param page           Número de página (por defecto 0)
     * @param size           Tamaño de la página (por defecto 10)
     * @param authentication Objeto de autenticación para identificar al usuario
     * @return Página de LessonResponseDTO
     */
    @GetMapping("/course/{uuidCourse}")
    public ResponseEntity<Page<LessonResponseDTO>> getAllLessonsFromCourse(
            @PathVariable UUID uuidCourse,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(lessonService.getAllLessonsFromCourse(uuidCourse, page, size, user));
    }

    /**
     * Crea una nueva lección para un curso.
     * Solo el instructor del curso puede realizar esta acción.
     *
     * @param dto            Datos de la nueva lección
     * @param authentication Información del usuario autenticado
     * @return La lección creada
     */
    @PostMapping("")
    public ResponseEntity<LessonResponseDTO> createLesson(@Valid @RequestBody LessonRequestDTO dto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(lessonService.createLesson(dto, user));
    }

    /**
     * Actualiza una lección existente por su UUID.
     * Solo el instructor de la lección puede modificarla.
     *
     * @param uuid           UUID de la lección
     * @param dto            Nuevos datos para la lección
     * @param authentication Información del usuario autenticado
     * @return La lección actualizada
     */
    @PutMapping("{uuid}")
    public ResponseEntity<LessonResponseDTO> updateLesson(@PathVariable UUID uuid,
            @Valid @RequestBody LessonRequestDTO dto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(lessonService.updateLesson(uuid, dto, user));
    }

    /**
     * Elimina una lección existente por su UUID.
     * Solo el instructor del curso puede eliminarla.
     *
     * @param uuid           UUID de la lección
     * @param authentication Información del usuario autenticado
     * @return Respuesta sin contenido si se elimina correctamente
     */
    @DeleteMapping("{uuid}")
    public ResponseEntity<Void> deleteLesson(@PathVariable("uuid") UUID uuid,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        lessonService.deleteLesson(uuid, user);
        return ResponseEntity.noContent().build();
    }

}
