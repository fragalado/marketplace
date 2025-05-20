package com.api.marketplace.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.UserLoginDTO;
import com.api.marketplace.dtos.UserRegisterDTO;
import com.api.marketplace.enums.Role;
import com.api.marketplace.repositories.UserRepository;

/**
 * Servicio encargado de gestionar la autenticación y el registro de usuarios.
 */
@Service
public class AuthService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository usuarioRepositorio;

    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * Registra un nuevo usuario en la plataforma.
     * Realiza varias validaciones antes de guardar el usuario:
     * - Verifica si ya existe un email registrado
     * - Verifica si ya existe un username registrado
     * - Impide registrar usuarios con rol ADMIN
     *
     * @param input Objeto con los datos del nuevo usuario
     * @return Usuario guardado en la base de datos
     */
    public User signup(UserRegisterDTO input) {
        // Comprobar si ya existe un usuario con ese email
        if (usuarioRepositorio.existsByEmail(input.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con ese email");
        }

        // Comprobar si ya existe un usuario con ese username
        if (usuarioRepositorio.existsByUsername(input.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con ese nombre de usuario");
        }

        // Comprobar que no se está intentando registrar como ADMIN
        if (input.getRole() == Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No se puede registrar un usuario con rol ADMIN");
        }

        // Crear y guardar el nuevo usuario
        User user = new User();
        user.setUsername(input.getUsername());
        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setEmail(input.getEmail());
        user.setRole(input.getRole());
        user.setHashedPassword(passwordEncoder.encode(input.getPassword()));

        return usuarioRepositorio.save(user);
    }

    /**
     * Autentica a un usuario utilizando sus credenciales (email y contraseña).
     * Lanza excepción 401 si las credenciales no son válidas.
     *
     * @param input Objeto con las credenciales del usuario
     * @return El usuario autenticado
     */
    public User authenticate(UserLoginDTO input) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }

        return usuarioRepositorio.findByEmail(input.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }
}
