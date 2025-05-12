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

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
     * Registra un nuevo usuario.
     */
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody UserRegisterDTO dto) {
        User user = authService.signup(dto);
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TokenResponse(accessToken, refreshToken));
    }

    /**
     * Autentica un usuario existente.
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserLoginDTO dto) {
        User user = authService.authenticate(dto);
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
