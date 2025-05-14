package com.api.marketplace.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.marketplace.daos.Course;
import com.api.marketplace.daos.Purchase;
import com.api.marketplace.daos.User;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    Page<Purchase> findByUser(User user, Pageable pageable);

    boolean existsByUserAndCourse(User user, Course course);
}
