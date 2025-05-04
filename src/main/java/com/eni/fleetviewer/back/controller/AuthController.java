package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.LoginRequest;
import com.eni.fleetviewer.back.dto.LoginResponse;
import com.eni.fleetviewer.back.model.User;
import com.eni.fleetviewer.back.repository.UserRepository;
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
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            String jwt = jwtService.generateToken(user);

            return ResponseEntity.ok(new LoginResponse(jwt, user.getUsername(), user.getRole()));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Authentification échouée");
        }
    }
}
