package com.api.marketplace.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.marketplace.daos.Course;
import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.CourseAdminDTO;
import com.api.marketplace.dtos.CourseRequestDTO;
import com.api.marketplace.dtos.CourseResponseDTO;
import com.api.marketplace.dtos.CourseResponseLiteDTO;
import com.api.marketplace.enums.Category;
import com.api.marketplace.repositories.CourseRepository;

/**
 * Implementación del servicio de cursos.
 * Esta clase gestiona las operaciones relacionadas con los cursos como obtener,
 * crear, actualizar o eliminar.
 */
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;

    public CourseServiceImpl(CourseRepository courseRepository, ModelMapper modelMapper) {
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<CourseResponseLiteDTO> getAllPublishedCoursesPaginated(int page, int size, String title,
            String category) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> coursePage;

        // Aplicar filtros según estén presentes o no
        if (title != null && category != null) {
            coursePage = courseRepository.findByPublishedTrueAndTitleContainingIgnoreCaseAndCategory(
                    title, Category.valueOf(category.toUpperCase()), pageable);
        } else if (title != null) {
            coursePage = courseRepository.findByPublishedTrueAndTitleContainingIgnoreCase(title, pageable);
        } else if (category != null) {
            coursePage = courseRepository.findByPublishedTrueAndCategory(Category.valueOf(category.toUpperCase()),
                    pageable);
        } else {
            coursePage = courseRepository.findByPublishedTrue(pageable);
        }

        // Mapear entidades a DTOs
        return coursePage.map(course -> modelMapper.map(course, CourseResponseLiteDTO.class));
    }

    @Override
    public Page<CourseResponseLiteDTO> getPopularCourses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findByPublishedTrue(pageable)
                .map(course -> modelMapper.map(course, CourseResponseLiteDTO.class));
    }

    @Override
    public CourseResponseDTO getCourseById(int id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));
        return modelMapper.map(course, CourseResponseDTO.class);
    }

    @Override
    public CourseResponseDTO getCourseByUuid(UUID uuid) {
        Course course = courseRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));
        return modelMapper.map(course, CourseResponseDTO.class);
    }

    @Override
    public CourseResponseDTO createCourse(CourseRequestDTO dto, User instructor) {
        Course course = modelMapper.map(dto, Course.class);
        course.setUser(instructor); // asignamos el instructor
        Course saved = courseRepository.save(course); // guardamos en base de datos
        return modelMapper.map(saved, CourseResponseDTO.class); // devolvemos como DTO
    }

    @Override
    public CourseResponseDTO updateCourse(UUID uuid, CourseRequestDTO dto, User authenticatedUser) {
        Course course = courseRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));

        // Comprobar que el usuario autenticado es el propietario del curso
        if (course.getUser().getId() != authenticatedUser.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para modificar este curso");
        }

        // Actualizar los campos
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setPrice(dto.getPrice());
        course.setCategory(dto.getCategory());
        course.setThumbnail_url(dto.getThumbnail_url());
        course.setDurationMinutes(dto.getDurationMinutes());
        course.setLanguage(dto.getLanguage());
        course.setLevel(dto.getLevel());
        course.setPublished(dto.isPublished());
        course.setUpdated_at(LocalDateTime.now());

        Course updated = courseRepository.save(course);
        return modelMapper.map(updated, CourseResponseDTO.class);
    }

    @Override
    public void deleteCourse(UUID uuid, User authenticatedUser) {
        Course course = courseRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));

        if (course.getUser().getId() != authenticatedUser.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para modificar este curso");
        }

        courseRepository.delete(course);
    }

    @Override
    public Page<CourseAdminDTO> getAllAuthenticatedUserCourses(int page, int size, String title, String category,
            User userAuthenticated) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> courses;

        // Aplicar filtros dinámicamente
        if (title != null && category != null) {
            courses = courseRepository.findByUserAndTitleContainingIgnoreCaseAndCategory(
                    userAuthenticated, title, Category.valueOf(category), pageable);
        } else if (title != null) {
            courses = courseRepository.findByUserAndTitleContainingIgnoreCase(userAuthenticated, title, pageable);
        } else if (category != null) {
            courses = courseRepository.findByUserAndCategory(userAuthenticated, Category.valueOf(category), pageable);
        } else {
            courses = courseRepository.findByUser(userAuthenticated, pageable);
        }

        return courses.map(course -> modelMapper.map(course, CourseAdminDTO.class));
    }
}
