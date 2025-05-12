package com.api.marketplace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.UserLoginDTO;
import com.api.marketplace.dtos.UserRegisterDTO;
import com.api.marketplace.jwt.AuthService;
import com.api.marketplace.jwt.JwtService;
import com.api.marketplace.jwt.TokenResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class SessionController {

    private final AuthService authService;
    private final JwtService jwtService;

    public SessionController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    /**
     * Registra un nuevo usuario.
     */
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody UserRegisterDTO dto) {
        User user = authService.signup(dto);
        String token = jwtService.generateToken(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TokenResponse(token));
    }

    /**
     * Autentica un usuario existente.
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserLoginDTO dto) {
        User user = authService.authenticate(dto);
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new TokenResponse(token));
    }

}
