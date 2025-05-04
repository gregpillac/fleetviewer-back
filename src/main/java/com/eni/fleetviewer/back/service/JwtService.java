package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.model.User;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;
import java.util.Date;

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
     * @param user l'utilisateur pour lequel générer le token
     * @return le token JWT généré
     */
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        try {
            return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", user.getRole())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
        } catch (Exception e) {
            logger.error("Erreur lors de la génération du token JWT", e);
            throw new RuntimeException("Impossible de générer le token JWT", e);
        }
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
     * @param token le token à vérifier
     * @param expectedUsername le nom d'utilisateur attendu
     * @return true si le token est valide, false sinon
     */
    public boolean isTokenValid(String token, String expectedUsername) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject();
            Date expiration = claims.getExpiration();

            return (username.equals(expectedUsername) && !expiration.before(new Date()));
        } catch (Exception e) {
            logger.error("Erreur lors de la validation du token JWT", e);
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
