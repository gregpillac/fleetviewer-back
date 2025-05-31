package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.AuthRequest;
import com.eni.fleetviewer.back.dto.AuthResponse;
import com.eni.fleetviewer.back.service.AuthService;
import com.eni.fleetviewer.back.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    /**
     * Contrôleur gérant l'Authentification des utilisateurs via un nom d'utilisateur et un mot de passe
     * Retourne un token JWT si l'authentification réussit
     * @return Réponse contenant le token JWT, le nom d'utilisateur et son rôle
     */
    public AuthController(JwtService jwtService, AuthService authService) {
        this.jwtService = jwtService;
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        Authentication auth = authService.authenticate(request);
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String jwt = jwtService.generateToken(auth);

        ResponseCookie jwtCookie = ResponseCookie.from("JWT_TOKEN", jwt)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return ResponseEntity.ok(new AuthResponse(
                userDetails.getUsername(),
                userDetails.getAuthorities().stream()
                        .findFirst()
                        .map(GrantedAuthority::getAuthority)
                        .orElse("ROLE_DEFAULT")
        ));
    }


    @GetMapping("/verify")
    public ResponseEntity<AuthResponse> verifyToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT_TOKEN")) {
                    String token = cookie.getValue();
                    if (jwtService.isTokenValid(token)) {
                        List<String> roles = jwtService.extractRoles(token);
                        // La réponse actuelle inclut seulement le premier rôle
                        String primaryRole = roles.isEmpty() ? null : roles.getFirst();
                        return ResponseEntity.ok(new AuthResponse(true, primaryRole));
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponse(false, null));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie jwtCookie = ResponseCookie.from("JWT_TOKEN", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        return ResponseEntity.ok().build();
    }
}
