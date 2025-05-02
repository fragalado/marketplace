package com.api.marketplace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.CourseRequestDTO;
import com.api.marketplace.dtos.CourseResponseDTO;
import com.api.marketplace.services.CourseServiceImpl;

import org.springframework.data.domain.Page;
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
@RequestMapping("/courses")
public class CourseController {

    // Methods to create:
    // 1 - Get all courses -> OK
    // 2 - Get course by id -> OK
    // 3 - Create course -> OK
    // 4 - Update course -> OK
    // 5 - Delete course -> OK
    // 6 - Get all courses by category
    // 7 - Get all courses by instructor

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
    public ResponseEntity<Page<CourseResponseDTO>> getAllCourses(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // Obtenemos todos los cursos de la base de datos
        Page<CourseResponseDTO> courses = courseService.getAllCoursesPaginated(page, size);
        return ResponseEntity.ok(courses);
    }

    /**
     * Metodo que devuelve un curso por su id
     * 
     * @param id Id del curso a devolver
     * @return Devuelve un objeto con los datos del curso
     */
    @GetMapping("{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable int id) {
        try {
            // Obtenemos el curso por id y lo devolvemos
            return ResponseEntity.ok().body(courseService.getCourseById(id));
        } catch (RuntimeException e) {
            // Si no existe, devolvemos un error 404
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Si ocurre un error, devolvemos un error 500
            return ResponseEntity.status(500).build();
        }
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
        // Obtenemos el usuario autenticado
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok().body(courseService.createCourse(dto, user));
    }

    /**
     * Metodo para actualizar curso
     * 
     * @param id  Id del curso a actualizar
     * @param dto Objeto con los nuevos valores del curso
     * @return Objeto con los datos del curso actualizado
     */
    @PutMapping("{id}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable int id, @RequestBody CourseRequestDTO dto) {
        try {
            return ResponseEntity.ok().body(courseService.updateCourse(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Metodo que elimina un curso por su id
     * 
     * @param id Id del curso a eliminar
     * @return Devuelve si se ha eliminado correctamente o no
     */
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable int id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok().body("Course deleted succesfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
