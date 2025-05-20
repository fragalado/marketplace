package com.api.marketplace.config;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.api.marketplace.jwt.JwtService;

/**
 * Filtro personalizado de autenticación que se ejecuta una vez por solicitud.
 * Extrae y valida el token JWT enviado en la cabecera Authorization.
 * Si es válido, autentica al usuario en el contexto de seguridad de Spring.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    JwtService jwtService;

    @Autowired
    UserDetailsService userDetailsService;

    /**
     * Método principal del filtro. Se ejecuta por cada petición entrante.
     *
     * @param request     La petición HTTP
     * @param response    La respuesta HTTP
     * @param filterChain Cadena de filtros de Spring Security
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String token = getTokenFromRequest(request);

        // Si no hay token o ya hay un usuario autenticado, continúa sin hacer nada
        if (token == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extraer el email del token
            String email = jwtService.getUsernameFromToken(token);

            // Cargar detalles del usuario desde la base de datos
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Validar el token con los datos del usuario
            if (jwtService.isTokenValid(token, userDetails)) {
                // Crear objeto de autenticación y establecerlo en el contexto
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (Exception e) {
            // Si el token no es válido, se devuelve un error 401 (no autorizado)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token inválido o expirado\"}");
            return;
        }

        // Continuar con el siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT de la cabecera Authorization.
     *
     * @param request La petición HTTP
     * @return El token JWT o null si no está presente o no tiene el formato
     *         correcto
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Quitar el prefijo "Bearer "
        }

        return null;
    }
}
