package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.LoginRequest;
import com.eni.fleetviewer.back.dto.LoginResponse;
import com.eni.fleetviewer.back.model.AppUser;
import com.eni.fleetviewer.back.repository.AppUserRepository;
import com.eni.fleetviewer.back.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AppUserRepository appUserRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, AppUserRepository appUserRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.appUserRepository = appUserRepository;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            AppUser user = appUserRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            String jwt = jwtService.generateToken(user);

            return ResponseEntity.ok(new LoginResponse(jwt, user.getUsername(), user.getRoleName()));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Authentification échouée");
        }
    }
}
