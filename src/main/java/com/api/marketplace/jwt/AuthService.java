package com.api.marketplace.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.UserLoginDTO;
import com.api.marketplace.dtos.UserRegisterDTO;
import com.api.marketplace.enums.Role;
import com.api.marketplace.repositories.UserRepository;

@Service
public class AuthService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository usuarioRepositorio;
    @Autowired
    AuthenticationManager authenticationManager;

    public User signup(UserRegisterDTO input) {
        User user = new User();

        user.setUsername(input.getUsername());
        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setEmail(input.getEmail());
        // Comprobamos que el role no sea ADMIN
        if (input.getRole() == Role.ADMIN) {
            throw new RuntimeException("No se puede registrar un usuario con rol ADMIN");
        }
        user.setRole(input.getRole());
        user.setHashedPassword(passwordEncoder.encode(input.getPassword()));

        return usuarioRepositorio.save(user);
    }

    public User authenticate(UserLoginDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));

        return usuarioRepositorio.findByEmail(input.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
