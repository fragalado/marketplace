package com.api.marketplace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.UserDTO;
import com.api.marketplace.dtos.UserUpdateRequestDTO;
import com.api.marketplace.services.UserServiceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * Método que devuelve la información de un usuario
     */
    @GetMapping("/info")
    public ResponseEntity<UserDTO> profileInfo(Authentication authentication) {
        // Obtenemos el usuario autenticado que hace la petición
        User user = (User) authentication.getPrincipal();

        // Pasamos el usuario a DTO
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole(),
                user.getCreated_at(), user.getUpdated_at());
        return ResponseEntity.ok().body(userDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateProfile(@RequestBody UserUpdateRequestDTO userDTO,
            Authentication authentication) {
        // Obtenemos el usuario autenticado que hace la petición
        User user = (User) authentication.getPrincipal();
        System.out.println("User: " + user.getUsername());
        System.out.println("User: " + user.getEmail());

        // Actualizamos el usuario
        return ResponseEntity.ok().body(userService.updateUser(userDTO, user));
    }

    @GetMapping("/delete")
    public ResponseEntity<String> deleteProfile(Authentication authentication) {
        // Obtenemos el usuario autenticado que hace la petición
        User user = (User) authentication.getPrincipal();

        // Llamamos al servicio para eliminar el usuario
        if (userService.deleteUser(user.getId())) {
            return ResponseEntity.ok().body("User deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Error deleting user");
        }
    }

}
