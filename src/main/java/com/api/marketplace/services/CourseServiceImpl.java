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
import com.api.marketplace.dtos.CourseRequestDTO;
import com.api.marketplace.dtos.CourseResponseDTO;
import com.api.marketplace.dtos.CourseResponseLiteDTO;
import com.api.marketplace.enums.Category;
import com.api.marketplace.repositories.CourseRepository;

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

        // Aquí puedes usar métodos dinámicos con Spring Data JPA, por ejemplo:
        Page<Course> coursePage;

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

        return coursePage.map(course -> modelMapper.map(course, CourseResponseLiteDTO.class));
    }

    @Override
    public Page<CourseResponseDTO> getPopularCourses(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findByPublishedTrue(pageable)
                .map(course -> modelMapper.map(course, CourseResponseDTO.class));
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
        // Convertimos el DTO a DAO
        Course course = modelMapper.map(dto, Course.class);
        // Le asignamos el instructor
        course.setUser(instructor);
        // Lo guardamos y el resultado lo almacenamos en una variable
        Course saved = courseRepository.save(course);
        // Devolvemos el resultado almacenado como DTO
        return modelMapper.map(saved, CourseResponseDTO.class);
    }

    @Override
    public CourseResponseDTO updateCourse(UUID uuid, CourseRequestDTO dto, User authenticatedUser) {
        // 1. Buscar el curso o lanzar 404 si no existe
        Course course = courseRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));

        // 2. Verificar que el curso pertenece al usuario autenticado
        if (course.getUser().getId() != authenticatedUser.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para modificar este curso");
        }

        // 3. Actualizamos los campos
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

        // 4. Guardar y devolver el resultado
        Course updated = courseRepository.save(course);
        return modelMapper.map(updated, CourseResponseDTO.class);
    }

    @Override
    public void deleteCourse(UUID uuid, User authenticatedUser) {
        // 1. Buscar el curso o lanzar 404 si no existe
        Course course = courseRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));

        // 2. Verificar que el curso pertenece al usuario autenticado
        if (course.getUser().getId() != authenticatedUser.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para modificar este curso");
        }

        // Si existe lo eliminamos
        courseRepository.delete(course);
    }

    @Override
    public Page<CourseResponseDTO> getAllAuthenticatedUserCourses(int page, int size, String title, String category,
            User userAuthenticated) {
        Pageable pageable = PageRequest.of(page, size);

        // Si hay filtros, aplicar con método personalizado
        Page<Course> courses;

        if (title != null && category != null) {
            courses = courseRepository.findByUserAndTitleContainingIgnoreCaseAndCategory(userAuthenticated, title,
                    Category.valueOf(category), pageable);
        } else if (title != null) {
            courses = courseRepository.findByUserAndTitleContainingIgnoreCase(userAuthenticated, title, pageable);
        } else if (category != null) {
            courses = courseRepository.findByUserAndCategory(userAuthenticated, Category.valueOf(category), pageable);
        } else {
            courses = courseRepository.findByUser(userAuthenticated, pageable);
        }

        return courses.map(course -> modelMapper.map(course, CourseResponseDTO.class));
    }

}
