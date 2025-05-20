package com.api.marketplace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.PurchaseRequestDTO;
import com.api.marketplace.services.PurchaseService;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controlador REST para gestionar las compras de cursos por parte de los
 * usuarios.
 */
@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    /**
     * Permite al usuario autenticado realizar la compra de uno o varios cursos.
     * 
     * @param request        Objeto con la lista de UUIDs de cursos a comprar
     * @param authentication Objeto de autenticación que contiene al usuario
     * @return Código 201 si la compra fue exitosa, o un error con su motivo
     */
    @PostMapping("")
    public ResponseEntity<?> buyCourses(@Valid @RequestBody PurchaseRequestDTO request, Authentication authentication) {
        try {
            // Obtenemos el usuario autenticado
            User user = (User) authentication.getPrincipal();

            // Realizamos la compra de los cursos
            int numPurchased = purchaseService.purchaseCourses(request, user);

            // Devolvemos el número de cursos comprados
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("purchasedCourses", numPurchased));

        } catch (ResponseStatusException e) {
            // Si ocurre un error controlado (como curso no encontrado), devolver código y
            // mensaje
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("error", e.getReason()));
        } catch (Exception e) {
            // Para errores inesperados, devolvemos error 500 con mensaje genérico
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error inesperado al procesar la compra."));
        }
    }

}
