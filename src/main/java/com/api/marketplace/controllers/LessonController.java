package com.api.marketplace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.LessonRequestDTO;
import com.api.marketplace.dtos.LessonResponseDTO;
import com.api.marketplace.services.LessonServiceImpl;

import java.util.List;
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

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonServiceImpl lessonService;

    public LessonController(LessonServiceImpl lessonServiceImpl) {
        this.lessonService = lessonServiceImpl;
    }

    /**
     * Get Lesson by Id
     * 
     * @param id Id of the Lesson
     * @return ResponseEntity<LessonResponseDTO>
     */
    @GetMapping("{uuid}")
    public ResponseEntity<LessonResponseDTO> getLessonByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(lessonService.getLessonByUuid(uuid));
    }

    /**
     * Get All Lessons
     * 
     * @return ResponseEntity<List<LessonResponseDTO>>
     */
    @GetMapping("")
    public ResponseEntity<List<LessonResponseDTO>> getAllLessons() {
        return ResponseEntity.ok(lessonService.getAllLessons());
    }

    /**
     * Get All Lessons from Course
     * 
     * @param idCourse Id of the Course
     * @return ResponseEntity<Page<LessonResponseDTO>>
     */
    @GetMapping("/course/{uuidCourse}")
    public ResponseEntity<Page<LessonResponseDTO>> getAllLessonsFromCourse(@PathVariable UUID uuidCourse,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(lessonService.getAllLessonsFromCourse(uuidCourse, page, size));
    }

    /**
     * Create new Lesson
     * 
     * @param dto LessonRequestDTO object containing the lesson data
     * @return ResponseEntity<LessonResponseDTO>
     */
    @PostMapping("")
    public ResponseEntity<LessonResponseDTO> createLesson(@RequestBody LessonRequestDTO dto) {
        return ResponseEntity.ok(lessonService.createLesson(dto));
    }

    /**
     * Update Lesson by Id
     * 
     * @param id  Id of the Lesson
     * @param dto LessonRequestDTO object containing the lesson data
     * @return ResponseEntity<LessonResponseDTO>
     */
    @PutMapping("{uuid}")
    public ResponseEntity<LessonResponseDTO> updateLesson(@PathVariable UUID uuid, @RequestBody LessonRequestDTO dto,
            Authentication authentication) {
        // Obtenemos el usuario autenticado
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(lessonService.updateLesson(uuid, dto, user));
    }

    /**
     * Delete Lesson by Id
     * 
     * @param idLesson Id of the Lesson
     * @return ResponseEntity<String>
     */
    @DeleteMapping("{uuid}")
    public ResponseEntity<Void> deleteLesson(@PathVariable("uuid") UUID uuid, Authentication authentication) {

        // Obtenemos el usuario autenticado
        User user = (User) authentication.getPrincipal();
        lessonService.deleteLesson(uuid, user);
        return ResponseEntity.noContent().build();
    }

}
