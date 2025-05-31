package com.eni.fleetviewer.back.service;

import io.jsonwebtoken.JwtException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${app.jwt.secret}")
    private String secretKeyString;

    @Value("${app.jwt.expiration}")
    private long expirationMs;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // Initialiser la clé secrète une seule fois au démarrage
        secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());

        // Vérification que la clé est suffisamment longue
        if (secretKeyString.length() < 32) {
            logger.warn("La clé JWT est trop courte.");
        }
    }


    /**
     * Génère un token JWT pour un utilisateur
     * @return le token JWT généré
     */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Extrait le nom d'utilisateur depuis un token JWT
     * @param token le token JWT
     * @return le nom d'utilisateur extrait
     */
    public String extractUsername(String token) {
        try {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        } catch (JwtException e) {
            logger.error("Erreur lors de l'extraction du username du token JWT", e);
            return null;
        }
    }

    /**
     * Extrait les rôles de l'utilisateur depuis un token JWT
     * @param token le token JWT
     * @return la liste des rôles de l'utilisateur
     */
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        if (claims == null) return Collections.emptyList();
        String roles = claims.get("roles", String.class);
        return roles != null ? Arrays.asList(roles.split(",")) : Collections.emptyList();
    }


    /**
     * @param token le token à vérifier
     * @return true si le token est valide et non expiré, false sinon
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims != null && !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            logger.error("Erreur JWT : {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extrait toutes les claims du token
     * @param token le token JWT
     * @return les claims du token ou null en cas d'erreur
     */
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            logger.error("Erreur lors de l'extraction des claims du token JWT", e);
            return null;
        }
    }
}
