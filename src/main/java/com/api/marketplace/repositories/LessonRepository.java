package com.api.marketplace.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.marketplace.daos.Course;
import com.api.marketplace.daos.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    Page<Lesson> findByCourse(Course course, Pageable pageable);

    Optional<Lesson> findByUuid(UUID uuid);

}
