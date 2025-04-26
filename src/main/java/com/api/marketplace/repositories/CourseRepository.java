package com.api.marketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.marketplace.daos.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

}
