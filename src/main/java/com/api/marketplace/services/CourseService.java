package com.api.marketplace.services;

import org.springframework.data.domain.Page;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.CourseRequestDTO;
import com.api.marketplace.dtos.CourseResponseDTO;

public interface CourseService {

    /**
     * Retrieves a paginated list of all courses
     * 
     * @param page   Page number to retrieve
     * @param size   Number of items per page
     * @param filter Filter to apply to the courses
     * @return A page of CourseResponseDTO objects
     */
    Page<CourseResponseDTO> getAllCoursesPaginated(int page, int size);

    CourseResponseDTO getCourseById(int id);

    CourseResponseDTO createCourse(CourseRequestDTO dto, User instructor);

    CourseResponseDTO updateCourse(int id, CourseRequestDTO dto);

    void deleteCourse(int id);
}
