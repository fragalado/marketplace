package com.api.marketplace.services;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.marketplace.daos.Course;
import com.api.marketplace.daos.Purchase;
import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.PurchaseRequestDTO;
import com.api.marketplace.repositories.CourseRepository;
import com.api.marketplace.repositories.PurchaseRepository;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CourseRepository courseRepository;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, CourseRepository courseRepository) {
        this.purchaseRepository = purchaseRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public void purchaseCourses(PurchaseRequestDTO request, User user) {
        if (request.getCourseUuids() == null || request.getCourseUuids().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La lista de cursos está vacía.");
        }

        for (UUID courseUuid : request.getCourseUuids()) {
            Course course = courseRepository.findByUuid(courseUuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Curso con UUID " + courseUuid + " no encontrado."));

            // Verificamos si ya fue comprado
            boolean alreadyPurchased = purchaseRepository.existsByUserAndCourse(user, course);
            if (alreadyPurchased) {
                // Saltamos este curso sin lanzar excepción
                continue;
            }

            Purchase purchase = new Purchase();
            purchase.setUser(user);
            purchase.setCourse(course);
            purchase.setPricePaid(course.getPrice());
            purchase.setPurchaseDate(LocalDate.now());
            purchase.setUuid(UUID.randomUUID());

            try {
                purchaseRepository.save(purchase);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error al guardar la compra del curso: " + course.getTitle(), e);
            }
        }
    }

}
