package com.api.marketplace.services;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.CourseRequestDTO;
import com.api.marketplace.dtos.CourseResponseDTO;
import com.api.marketplace.dtos.CourseResponseLiteDTO;

public interface CourseService {

    /**
     * Retrieves a paginated list of all published courses
     * 
     * @param page   Page number to retrieve
     * @param size   Number of items per page
     * @param filter Filter to apply to the courses
     * @return A page of CourseResponseDTO objects
     */
    Page<CourseResponseLiteDTO> getAllPublishedCoursesPaginated(int page, int size);

    Page<CourseResponseDTO> getAllAuthenticatedUserCourses(int page, int size, User userAuthenticated);

    /**
     * Retrieves popular courses (published) paginated
     * 
     * @param page Page number to retrieve
     * @param size Number of items per page
     * @return A page of CourseResponseDTO objects
     */
    Page<CourseResponseDTO> getPopularCourses(int page, int size);

    CourseResponseDTO getCourseById(int id);

    CourseResponseDTO getCourseByUuid(UUID uuid);

    CourseResponseDTO createCourse(CourseRequestDTO dto, User instructor);

    CourseResponseDTO updateCourse(UUID uuid, CourseRequestDTO dto, User authenticatedUser);

    void deleteCourse(UUID uuid, User authenticatedUser);
}
