package com.api.marketplace.services;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.marketplace.daos.Course;
import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.CourseRequestDTO;
import com.api.marketplace.dtos.CourseResponseDTO;
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
    public Page<CourseResponseDTO> getAllPublishedCoursesPaginated(int page, int size) {
        // Creamos una pageable
        Pageable pageable = PageRequest.of(page, size);

        // Obtenemos la página de cursos
        // Page<Course> coursePage = courseRepository.findAll(pageable);
        Page<Course> coursePage = courseRepository.findByPublishedTrue(pageable);

        // Convertimos la página de cursos a una página de CourseResponseDTO
        Page<CourseResponseDTO> courseDtoPage = coursePage
                .map(course -> modelMapper.map(course, CourseResponseDTO.class));

        // Devolvemos la página
        return courseDtoPage;
    }

    @Override
    public CourseResponseDTO getCourseById(int id) throws RuntimeException {
        // Obtenemos el curso por id
        Optional<Course> course = courseRepository.findById(id);

        // Comprobamos que el curso existe
        if (course.isPresent()) {
            return modelMapper.map(course, CourseResponseDTO.class);
        } else {
            // Si no existe, lanzamos una excepción
            throw new RuntimeException("Course not found");
        }
    }

    @Override
    public CourseResponseDTO createCourse(CourseRequestDTO dto, User instructor) {
        // Convertimos el DTO a DAO
        Course course = modelMapper.map(dto, Course.class);

        // Asignamos el instructor al curso
        course.setUser(instructor);

        // Guardamos el curso
        Course courseSaved = courseRepository.save(course);

        // Devolvemos el curso guardado como DTO
        return modelMapper.map(courseSaved, CourseResponseDTO.class);
    }

    @Override
    public CourseResponseDTO updateCourse(int id, CourseRequestDTO dto) {
        // Obtenemos el curso por el id
        Optional<Course> courseFind = courseRepository.findById(id);

        // Comprobamos si se ha obtenido el curso
        if (courseFind.isEmpty()) {
            throw new RuntimeException("Course not found.");
        }

        // Si existe le añadimos los nuevos valores
        Course course = courseFind.get();
        if (dto.getTitle() != null) {
            course.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            course.setDescription(dto.getDescription());
        }
        if (dto.getPrice() > 0) {
            course.setPrice(dto.getPrice());
        }
        if (dto.getCategory() != null) {
            course.setCategory(dto.getCategory());
        }
        if (dto.getThumbnail_url() != null) {
            course.setThumbnail_url(dto.getThumbnail_url());
        }
        if (dto.getDurationMinutes() > 0) {
            course.setDurationMinutes(dto.getDurationMinutes());
        }
        if (dto.getLanguage() != null) {
            course.setLanguage(dto.getLanguage());
        }
        if (dto.getLevel() != null) {
            course.setLevel(dto.getLevel());
        }
        if (dto.isPublished() != course.isPublished()) {
            course.setPublished(dto.isPublished());
        }

        // Actualizamos el curso y lo devolvemos como ResponseDTO
        return modelMapper.map(courseRepository.save(course), CourseResponseDTO.class);
    }

    @Override
    public void deleteCourse(int id) {
        // Obtenemos el curso y comprobamos si se encuentra o no
        Optional<Course> courseFind = courseRepository.findById(id);

        if (courseFind.isEmpty()) {
            throw new RuntimeException("Course not found.");
        }

        // Si existe lo eliminamos
        courseRepository.deleteById(id);
    }

    @Override
    public Page<CourseResponseDTO> getAllAuthenticatedUserCourses(int page, int size, User userAuthenticated) {
        // Creamos una pageable
        Pageable pageable = PageRequest.of(page, size);

        // Obtenemos la página de cursos
        // Page<Course> coursePage = courseRepository.findAll(pageable);
        Page<Course> coursePage = courseRepository.findByUser(userAuthenticated, pageable);

        // Convertimos la página de cursos a una página de CourseResponseDTO
        Page<CourseResponseDTO> courseDtoPage = coursePage
                .map(course -> modelMapper.map(course, CourseResponseDTO.class));

        // Devolvemos la página
        return courseDtoPage;
    }

}
