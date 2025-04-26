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
    public Page<CourseResponseDTO> getAllCoursesPaginated(int page, int size) {
        // Creamos una pageable
        Pageable pageable = PageRequest.of(page, size);

        // Obtenemos la página de cursos
        Page<Course> coursePage = courseRepository.findAll(pageable);

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
    public CourseResponseDTO updateCourse(Course course, CourseResponseDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCourse'");
    }

    @Override
    public boolean deleteCourse(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteCourse'");
    }

}
