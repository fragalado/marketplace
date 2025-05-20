package com.api.marketplace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.CourseResponseLiteDTO;
import com.api.marketplace.dtos.UserDTO;
import com.api.marketplace.dtos.UserUpdateRequestDTO;
import com.api.marketplace.services.UserServiceImpl;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador REST que gestiona las operaciones relacionadas con el usuario
 * autenticado,
 * como obtener su perfil, actualizarlo, eliminarlo o consultar los cursos
 * comprados.
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
     * Obtiene la información del usuario actualmente autenticado.
     *
     * @param authentication Objeto de autenticación con los datos del usuario
     * @return UserDTO con los datos del usuario autenticado
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> profileInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * Actualiza los datos del usuario actualmente autenticado.
     *
     * @param userDTO        Datos nuevos del usuario
     * @param authentication Autenticación del usuario actual
     * @return UserDTO actualizado
     */
    @PutMapping("")
    public ResponseEntity<UserDTO> updateProfile(@Valid @RequestBody UserUpdateRequestDTO userDTO,
            Authentication authentication) {
        System.out.println("Ha entrado en update profile");
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userService.updateUser(userDTO, user));
    }

    /**
     * Elimina permanentemente la cuenta del usuario autenticado.
     *
     * @param authentication Autenticación del usuario actual
     * @return Respuesta vacía si se eliminó correctamente
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

    /**
     * Devuelve los cursos comprados por el usuario autenticado, paginados.
     *
     * @param page           Número de página
     * @param size           Tamaño de página
     * @param authentication Autenticación del usuario actual
     * @return Página con los cursos comprados
     */
    @GetMapping("me/courses")
    public ResponseEntity<Page<CourseResponseLiteDTO>> getPurchasedCourses(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getPurchasedCourses(page, size, user));
    }

    /**
     * Devuelve solo los UUID de los cursos comprados por el usuario.
     *
     * @param authentication Autenticación del usuario actual
     * @return Lista de UUIDs de los cursos comprados
     */
    @GetMapping("me/courses/uuids")
    public ResponseEntity<List<UUID>> getUuidPurchasedCourses(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getUuidPurchasedCourses(user));
    }
}
