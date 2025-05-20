package com.api.marketplace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.UserLoginDTO;
import com.api.marketplace.dtos.UserRegisterDTO;
import com.api.marketplace.jwt.AuthService;
import com.api.marketplace.jwt.JwtService;
import com.api.marketplace.jwt.TokenResponse;
import com.api.marketplace.repositories.UserRepository;

import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controlador REST responsable de gestionar la autenticación de usuarios,
 * incluyendo el registro, inicio de sesión y renovación de tokens JWT.
 */
@RestController
@RequestMapping("/auth")
public class SessionController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public SessionController(AuthService authService, JwtService jwtService, UserRepository userRepository) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    /**
     * Registra un nuevo usuario y genera tokens de acceso y refresh.
     *
     * @param dto Objeto con los datos del nuevo usuario
     * @return TokenResponse con el accessToken y refreshToken
     */
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody UserRegisterDTO dto) {
        User user = authService.signup(dto);
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TokenResponse(accessToken, refreshToken));
    }

    /**
     * Autentica un usuario existente y genera tokens si las credenciales son
     * válidas.
     *
     * @param dto DTO con el email y contraseña del usuario
     * @return TokenResponse con el accessToken y refreshToken
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody UserLoginDTO dto) {
        User user = authService.authenticate(dto);
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

    /**
     * Refresca el token de acceso si el refresh token es válido.
     *
     * @param request Mapa con la clave "refreshToken"
     * @return Nuevo accessToken y refreshToken o error 401 si el token no es válido
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        try {
            String email = jwtService.getUsernameFromToken(refreshToken);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            String newAccessToken = jwtService.generateToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(user);

            return ResponseEntity.ok(new TokenResponse(newAccessToken, newRefreshToken));

        } catch (JwtException e) {
            // Token inválido o expirado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
