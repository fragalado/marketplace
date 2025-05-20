package com.api.marketplace.services;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.PurchaseRequestDTO;

/**
 * Interfaz de servicio encargada de gestionar las compras de cursos por parte
 * de los usuarios.
 */
public interface PurchaseService {

    /**
     * Procesa la compra de uno o varios cursos por parte de un usuario.
     *
     * @param request Objeto que contiene una lista de UUIDs de los cursos a comprar
     * @param user    Usuario autenticado que realiza la compra
     * @return Número de cursos comprados exitosamente
     */
    int purchaseCourses(PurchaseRequestDTO request, User user);
}
