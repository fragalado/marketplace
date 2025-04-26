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

    @Autowired
    AuthService authenticationService;

    @Autowired
    JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody UserRegisterDTO registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody UserLoginDTO loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.getToken(authenticatedUser);

        return ResponseEntity.ok(new TokenResponse(jwtToken));
    }

}
