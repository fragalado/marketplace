package com.api.marketplace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.CourseRequestDTO;
import com.api.marketplace.dtos.CourseResponseDTO;
import com.api.marketplace.services.CourseServiceImpl;

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
 * Controlador para los cursos
 */
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseServiceImpl courseService;

    public CourseController(CourseServiceImpl courseService) {
        this.courseService = courseService;
    }

    /**
     * Metodo que devuelve todos los cursos de la base de datos paginados
     * 
     * @param page Número de pagina
     * @param size Tamanyo de la pagina
     * @return Devuelve una pagina con objetos de tipo curso
     */
    @GetMapping("")
    public ResponseEntity<Page<CourseResponseDTO>> getAllPublishedCourses(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // Obtenemos todos los cursos de la base de datos
        return ResponseEntity.ok(courseService.getAllPublishedCoursesPaginated(page, size));
    }

    @GetMapping("my-courses")
    public ResponseEntity<Page<CourseResponseDTO>> getAllAuthenticatedUserCourses(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        // Obtenemos el usuario autenticado
        User user = (User) authentication.getPrincipal();
        // Obtenemos todos los cursos del usuario autenticado
        return ResponseEntity.ok(courseService.getAllAuthenticatedUserCourses(page, size, user));
    }

    /**
     * Metodo que devuelve un curso por su id
     * 
     * @param id Id del curso a devolver
     * @return Devuelve un objeto con los datos del curso
     */
    @GetMapping("{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable int id) {
        // Obtenemos el curso por id y lo devolvemos
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    /**
     * Metodo que crea un curso pasado por parametros
     * 
     * @param dto            Objeto con los datos del nuevo curso
     * @param authentication Objeto Authentication que contiene informacion del
     *                       usuario autenticado
     * @return Devuelve un objeto CourseResponseDTO con los datos del curso creado
     */
    @PostMapping("")
    public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody CourseRequestDTO dto,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        CourseResponseDTO created = courseService.createCourse(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Metodo para actualizar curso
     * 
     * @param id  Id del curso a actualizar
     * @param dto Objeto con los nuevos valores del curso
     * @return Objeto con los datos del curso actualizado
     */
    @PutMapping("{id}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable int id, @RequestBody CourseRequestDTO dto,
            Authentication authentication) {

        // Obtenemos el usuario autenticado
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(courseService.updateCourse(id, dto, user));
    }

    /**
     * Metodo que elimina un curso por su id
     * 
     * @param id Id del curso a eliminar
     * @return Devuelve si se ha eliminado correctamente o no
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id, Authentication authentication) {
        // Obtenemos el usuario autenticado
        User user = (User) authentication.getPrincipal();
        courseService.deleteCourse(id, user);
        return ResponseEntity.noContent().build();
    }

}
