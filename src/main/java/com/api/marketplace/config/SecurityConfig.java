package com.api.marketplace.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Clase de configuración principal de Spring Security.
 * Define la política de autenticación, CORS, CSRF, endpoints públicos y
 * filtros.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig {

        @Autowired
        AuthenticationProvider authenticationProvider;

        @Autowired
        JwtAuthenticationFilter jwtAuthenticationFilter;

        /**
         * Define el filtro de seguridad principal (SecurityFilterChain).
         * Se encarga de configurar las reglas de autorización, CORS, CSRF,
         * autenticación, etc.
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                // Configuración CORS personalizada
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                                // Deshabilitamos CSRF porque trabajamos con JWT (no con sesiones/cookies)
                                .csrf(csrf -> csrf.disable())

                                // Configuración de las rutas públicas y privadas
                                .authorizeHttpRequests(authRequest -> authRequest
                                                // Endpoints de autenticación accesibles sin login
                                                .requestMatchers("/auth/**").permitAll()

                                                // Endpoints públicos GET (p. ej. cursos o lecciones públicas)
                                                .requestMatchers(HttpMethod.GET, "/api/courses", "/api/courses/{id}",
                                                                "/api/lessons/{id}")
                                                .permitAll()

                                                // Cualquier otra petición requiere autenticación
                                                .anyRequest().authenticated())

                                // Política de sesión: stateless (no se guarda sesión en el servidor)
                                .sessionManagement(sessionManager -> sessionManager
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                // Registramos el proveedor de autenticación (usuario + contraseña)
                                .authenticationProvider(authenticationProvider)

                                // Añadimos el filtro JWT antes del filtro estándar de autenticación por
                                // username y password
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                                .build();
        }

        /**
         * Configura CORS para permitir solicitudes desde el frontend.
         * Esta configuración permite que el frontend en localhost:4200 acceda a la API.
         */
        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                // Orígenes permitidos (donde corre tu frontend)
                configuration.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:4200"));

                // Métodos HTTP permitidos
                configuration.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT", "OPTIONS"));

                // Cabeceras permitidas
                configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

                // Permitir el envío de cookies o credenciales si fuera necesario
                configuration.setAllowCredentials(true);

                // Registramos la configuración para todas las rutas
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);

                return source;
        }
}
