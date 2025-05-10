package com.api.marketplace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.UserDTO;
import com.api.marketplace.dtos.UserUpdateRequestDTO;
import com.api.marketplace.services.UserServiceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controlador para Usuario
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;

    public UserController(UserServiceImpl userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    /**
     * Método para obtener la información del usuario autenticado
     * 
     * @param authentication Objeto Authentication que contiene el usuario
     *                       autenticado
     * @return Devuelve un objeto con la informacion del usuario autenticado
     */
    @GetMapping("/info")
    public ResponseEntity<UserDTO> profileInfo(Authentication authentication) {
        // Obtenemos el usuario autenticado que hace la petición
        User user = (User) authentication.getPrincipal();

        // Pasamos el usuario a DTO
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Método para actualizar la información del usuario autenticado
     * 
     * @param userDTO        Objeto con los nuevos datos del usuario
     * @param authentication Objeto Authetication que contiene el usuario
     *                       autenticado
     * @return Devuelve un objeto con la informacion del usuario autenticado
     */
    @PutMapping("")
    public ResponseEntity<UserDTO> updateProfile(@RequestBody UserUpdateRequestDTO userDTO,
            Authentication authentication) {
        // Obtenemos el usuario autenticado que hace la petición
        User user = (User) authentication.getPrincipal();

        // Actualizamos el usuario
        return ResponseEntity.ok(userService.updateUser(userDTO, user));
    }

    /**
     * Método que elimina el usuario autenticado
     * 
     * @param authentication Objeto Authentication que contiene la informacion del
     *                       usuario autenticado
     * @return Devuelve si el usuario se ha eliminado correctamente o no.
     */
    @DeleteMapping("")
    public ResponseEntity<Void> deleteProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        boolean deleted = userService.deleteUser(user.getId());

        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo eliminar el usuario");
        }

        return ResponseEntity.noContent().build(); // 204 sin cuerpo
    }

}
