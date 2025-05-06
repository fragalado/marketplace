package com.api.marketplace.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.UserLoginDTO;
import com.api.marketplace.dtos.UserRegisterDTO;
import com.api.marketplace.jwt.AuthService;
import com.api.marketplace.jwt.JwtService;
import com.api.marketplace.jwt.TokenResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class SessionController {

    // Methods
    // Register -> OK
    // Login -> OK

    @Autowired
    AuthService authenticationService;

    @Autowired
    JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody UserRegisterDTO registerUserDto) {
        try {
            User registeredUser = authenticationService.signup(registerUserDto);

            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody UserLoginDTO loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        System.out.println("USername: " + authenticatedUser.getUsername());
        String jwtToken = jwtService.getToken(authenticatedUser);

        return ResponseEntity.ok(new TokenResponse(jwtToken));
    }

}
