package com.api.marketplace.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.UserDTO;
import com.api.marketplace.repositories.UserRepository;

/**
 * Clase de configuración principal de la aplicación.
 * Define beans clave como el servicio de autenticación, codificador de
 * contraseñas,
 * proveedor de autenticación, gestor de autenticación y el mapeador de
 * entidades (ModelMapper).
 */
@Configuration
public class ApplicationConfiguration {

    @Autowired
    UserRepository usuarioRepositorio;

    /**
     * Bean que define cómo se cargan los detalles del usuario para el proceso de
     * autenticación.
     * En este caso, se autentica usando el email.
     *
     * @return UserDetailsService que obtiene un usuario por su email
     */
    @Bean
    UserDetailsService userDetailsService() {
        return username -> usuarioRepositorio.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Bean para codificar contraseñas usando BCrypt, un algoritmo robusto y seguro.
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean que proporciona el AuthenticationManager a partir de la configuración
     * actual de Spring Security.
     *
     * @param config Configuración de autenticación de Spring
     * @return AuthenticationManager que gestiona la autenticación de usuarios
     * @throws Exception si falla al obtener el AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Bean que configura el proveedor de autenticación.
     * Usa DaoAuthenticationProvider con UserDetailsService y BCryptPasswordEncoder.
     *
     * @return AuthenticationProvider que delega en el repositorio de usuarios
     */
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Bean para mapear entidades entre DAOs y DTOs usando ModelMapper.
     * Se especifica un mapeo personalizado entre los campos `usernameReal` y
     * `username`.
     *
     * @return ModelMapper personalizado
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(User.class, UserDTO.class).addMappings(mapper -> {
            mapper.map(User::getUsernameReal, UserDTO::setUsername);
        });
        return modelMapper;
    }
}
