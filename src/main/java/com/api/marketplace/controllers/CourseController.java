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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/course")
public class CourseController {

    // Methods to create:
    // 1 - Get all courses -> OK
    // 2 - Get course by id -> OK
    // 3 - Create course -> OK
    // 4 - Update course
    // 5 - Delete course
    // 6 - Get all courses by category
    // 7 - Get all courses by instructor

    private final CourseServiceImpl courseService;

    public CourseController(CourseServiceImpl courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<CourseResponseDTO>> getAllCourses(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // Obtenemos todos los cursos de la base de datos
        Page<CourseResponseDTO> courses = courseService.getAllCoursesPaginated(page, size);
        return ResponseEntity.ok(courses);
    }

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

    @PostMapping("/create")
    public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody CourseRequestDTO dto,
            Authentication authentication) {
        // Obtenemos el usuario autenticado
        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok().body(courseService.createCourse(dto, user));
    }

}
