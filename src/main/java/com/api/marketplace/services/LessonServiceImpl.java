package com.api.marketplace.services;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.marketplace.daos.Course;
import com.api.marketplace.daos.Lesson;
import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.LessonRequestDTO;
import com.api.marketplace.dtos.LessonResponseDTO;
import com.api.marketplace.repositories.CourseRepository;
import com.api.marketplace.repositories.LessonRepository;

/**
 * Implementación del servicio de lecciones.
 * Gestiona las operaciones CRUD relacionadas con las lecciones de los cursos
 * creados por instructores.
 */
@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    public LessonServiceImpl(LessonRepository lessonRepository, CourseRepository courseRepository,
            ModelMapper modelMapper) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<LessonResponseDTO> getAllLessonsFromCourse(UUID uuidCourse, int page, int size,
            User authenticatedUser) {
        Course course = courseRepository.findByUuid(uuidCourse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));

        // Verifica que el usuario sea el propietario del curso
        if (course.getUser().getId() != authenticatedUser.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No tienes permiso para ver estas lecciones");
        }

        Pageable pageable = PageRequest.of(page, size);
        return lessonRepository.findByCourse(course, pageable)
                .map(lesson -> modelMapper.map(lesson, LessonResponseDTO.class));
    }

    @Override
    public LessonResponseDTO getLessonByUuid(UUID uuidLesson) {
        Lesson lesson = lessonRepository.findByUuid(uuidLesson)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lección no encontrada"));

        return modelMapper.map(lesson, LessonResponseDTO.class);
    }

    @Override
    public LessonResponseDTO createLesson(LessonRequestDTO dto, User authenticatedUser) {
        Course course = courseRepository.findByUuid(dto.getIdCourse())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));

        // Verifica que el curso pertenezca al usuario autenticado
        if (course.getUser().getId() != authenticatedUser.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "No puedes añadir lecciones a un curso que no es tuyo");
        }

        // Crear y guardar la lección
        Lesson lesson = new Lesson();
        lesson.setTitle(dto.getTitle());
        lesson.setVideo_url(dto.getVideo_url());
        lesson.setDescription(dto.getDescription());
        lesson.setThumbnail_url(dto.getThumbnail_url());
        lesson.setDurationMinutes(dto.getDurationMinutes());
        lesson.setFreePreview(dto.isFreePreview());
        lesson.setPosition(dto.getPosition());
        lesson.setCourse(course);

        Lesson saved = lessonRepository.save(lesson);
        return modelMapper.map(saved, LessonResponseDTO.class);
    }

    @Override
    public LessonResponseDTO updateLesson(UUID uuidLesson, LessonRequestDTO dto, User authenticatedUser) {
        Lesson lesson = lessonRepository.findByUuid(uuidLesson)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lección no encontrada"));

        // Verifica que el usuario sea el propietario del curso
        if (lesson.getCourse().getUser().getId() != authenticatedUser.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para modificar esta lección");
        }

        // Actualizar los campos
        lesson.setTitle(dto.getTitle());
        lesson.setDescription(dto.getDescription());
        lesson.setThumbnail_url(dto.getThumbnail_url());
        lesson.setVideo_url(dto.getVideo_url());
        lesson.setDurationMinutes(dto.getDurationMinutes());
        lesson.setFreePreview(dto.isFreePreview());
        lesson.setPosition(dto.getPosition());

        return modelMapper.map(lessonRepository.save(lesson), LessonResponseDTO.class);
    }

    @Override
    public void deleteLesson(UUID uuidLesson, User authenticatedUser) {
        Lesson lesson = lessonRepository.findByUuid(uuidLesson)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));

        if (lesson.getCourse().getUser().getId() != authenticatedUser.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para modificar esta lección");
        }

        lessonRepository.delete(lesson);
    }
}
