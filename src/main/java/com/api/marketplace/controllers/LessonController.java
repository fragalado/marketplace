package com.api.marketplace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.marketplace.dtos.LessonRequestDTO;
import com.api.marketplace.dtos.LessonResponseDTO;
import com.api.marketplace.services.LessonServiceImpl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    // Methods
    // Get Lesson by ID -> OK
    // Get All Lessons -> OK
    // Create Lesson -> OK
    // Update Lesson -> OK
    // Delete Lesson -> OK

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
    @GetMapping("{id}")
    public ResponseEntity<LessonResponseDTO> getLessonById(@PathVariable int id) {
        try {
            return ResponseEntity.ok().body(lessonService.getLessonById(id));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get All Lessons
     * 
     * @return ResponseEntity<List<LessonResponseDTO>>
     */
    @GetMapping("")
    public ResponseEntity<List<LessonResponseDTO>> getAllLessons() {
        try {
            return ResponseEntity.ok().body(lessonService.getAllLessons());
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Create new Lesson
     * 
     * @param dto LessonRequestDTO object containing the lesson data
     * @return ResponseEntity<LessonResponseDTO>
     */
    @PostMapping("")
    public ResponseEntity<LessonResponseDTO> createNewLesson(@RequestBody LessonRequestDTO dto) {
        try {
            return ResponseEntity.ok().body(lessonService.createLesson(dto));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Update Lesson by Id
     * 
     * @param id  Id of the Lesson
     * @param dto LessonRequestDTO object containing the lesson data
     * @return ResponseEntity<LessonResponseDTO>
     */
    @PutMapping("{id}")
    public ResponseEntity<LessonResponseDTO> updateLesson(@PathVariable int id, @RequestBody LessonRequestDTO dto) {
        try {
            return ResponseEntity.ok().body(lessonService.updateLesson(id, dto));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Delete Lesson by Id
     * 
     * @param idLesson Id of the Lesson
     * @return ResponseEntity<String>
     */
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable("id") int idLesson) {
        try {
            lessonService.deleteLesson(idLesson);
            return ResponseEntity.ok().body("Lesson deleted succesfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

}
