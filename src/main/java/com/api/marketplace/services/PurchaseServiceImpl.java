package com.api.marketplace.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

/**
 * Implementación del servicio de compras.
 * Gestiona la lógica de compra de cursos por parte de los usuarios.
 */
@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CourseRepository courseRepository;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, CourseRepository courseRepository) {
        this.purchaseRepository = purchaseRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public int purchaseCourses(PurchaseRequestDTO request, User user) {

        // Verificamos si la lista de UUIDs es nula o vacía
        if (request.getCourseUuids() == null || request.getCourseUuids().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La lista de cursos está vacía.");
        }

        List<Purchase> purchasesToSave = new ArrayList<>();

        // Iteramos por cada UUID de curso recibido
        for (UUID courseUuid : request.getCourseUuids()) {
            // Verificamos si el curso existe
            Course course = courseRepository.findByUuid(courseUuid)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Curso con UUID " + courseUuid + " no encontrado."));

            // Si el usuario ya ha comprado este curso, lo ignoramos
            if (purchaseRepository.existsByUserAndCourse(user, course)) {
                continue;
            }

            // Creamos la compra
            Purchase purchase = new Purchase();
            purchase.setUser(user);
            purchase.setCourse(course);
            purchase.setPricePaid(course.getPrice());
            purchase.setPurchaseDate(LocalDate.now());
            purchase.setUuid(UUID.randomUUID());

            purchasesToSave.add(purchase);
        }

        // Guardamos todas las compras válidas
        purchaseRepository.saveAll(purchasesToSave);

        // Devolvemos el número total de compras realizadas
        return purchasesToSave.size();
    }
}
