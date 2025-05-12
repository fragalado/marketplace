package com.api.marketplace.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.api.marketplace.daos.User;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail()) // usamos el email como subject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 15)) // 15 minutos
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 7 días
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String email = getUsernameFromToken(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject); // devuelve email
    }

    public <T> T getClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(getAllClaims(token));
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return getClaim(token, Claims::getExpiration).before(new Date());
    }
}
