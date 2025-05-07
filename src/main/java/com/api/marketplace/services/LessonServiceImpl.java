package com.api.marketplace.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.hibernate.MappingException;
import org.modelmapper.ConfigurationException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.marketplace.daos.Course;
import com.api.marketplace.daos.Lesson;
import com.api.marketplace.dtos.LessonRequestDTO;
import com.api.marketplace.dtos.LessonResponseDTO;
import com.api.marketplace.repositories.CourseRepository;
import com.api.marketplace.repositories.LessonRepository;

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
    public List<LessonResponseDTO> getAllLessons()
            throws IllegalArgumentException, MappingException, ConfigurationException {
        return lessonRepository.findAll().stream().map(lesson -> modelMapper.map(lesson, LessonResponseDTO.class))
                .toList();
    }

    @Override
    public Page<LessonResponseDTO> getAllLessonsFromCourse(int idCourse, int page, int size) {
        // Obtenemos el curso por el id
        Optional<Course> findCourse = courseRepository.findById(idCourse);

        if (findCourse.isEmpty()) {
            throw new IllegalArgumentException("Course not found");
        }

        Course course = findCourse.get();
        // Obtenemos la pagina de lecciones por el curso
        Pageable pageable = PageRequest.of(page, size);
        Page<Lesson> lessons = lessonRepository.findByCourse(course, pageable);

        // Devolvemos la pagina convertida a DTO
        return lessons.map(lesson -> modelMapper.map(lesson, LessonResponseDTO.class));
    }

    @Override
    public LessonResponseDTO getLessonById(int idLesson)
            throws IllegalArgumentException, MappingException, ConfigurationException {
        Optional<Lesson> findLesson = lessonRepository.findById(idLesson);

        if (findLesson.isEmpty()) {
            throw new IllegalArgumentException("Lesson not found");
        }

        Lesson lesson = findLesson.get();

        return modelMapper.map(lesson, LessonResponseDTO.class);
    }

    @Override
    public LessonResponseDTO createLesson(LessonRequestDTO dto) throws IllegalArgumentException,
            ConfigurationException, MappingException, OptimisticLockingFailureException {

        Optional<Course> findCourse = courseRepository.findById(dto.getIdCourse());
        if (findCourse.isEmpty()) {
            throw new IllegalArgumentException("Course not found");
        }
        Course course = findCourse.get();
        Lesson newLesson = new Lesson();
        newLesson.setTitle(dto.getTitle());
        newLesson.setVideo_url(dto.getVideo_url());
        newLesson.setDescription(dto.getDescription());
        newLesson.setThumbnail_url(dto.getThumbnail_url());
        newLesson.setDurationMinutes(dto.getDurationMinutes());
        newLesson.setFreePreview(dto.isFreePreview());
        newLesson.setCourse(course);

        return modelMapper.map(lessonRepository.save(newLesson), LessonResponseDTO.class);
    }

    @Override
    public LessonResponseDTO updateLesson(int idLesson, LessonRequestDTO dto) {
        Optional<Lesson> findLesson = lessonRepository.findById(idLesson);

        if (findLesson.isEmpty()) {
            throw new IllegalArgumentException("Lesson not found");
        }

        Lesson lesson = findLesson.get();
        lesson.setTitle(dto.getTitle());
        lesson.setDescription(dto.getDescription());
        lesson.setThumbnail_url(dto.getThumbnail_url());
        lesson.setVideo_url(dto.getVideo_url());
        lesson.setDurationMinutes(dto.getDurationMinutes());

        return modelMapper.map(lessonRepository.save(lesson), LessonResponseDTO.class);
    }

    @Override
    public void deleteLesson(int idLesson)
            throws IllegalArgumentException, OptimisticLockingFailureException, NoSuchElementException {
        Optional<Lesson> findLesson = lessonRepository.findById(idLesson);

        if (findLesson.isEmpty()) {
            throw new IllegalArgumentException("Lesson not found");
        }

        Lesson lesson = findLesson.get();

        lessonRepository.delete(lesson);
    }

}
