package com.api.marketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.marketplace.daos.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {

}
