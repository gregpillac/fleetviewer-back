package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.AppUserDTO;
import com.eni.fleetviewer.back.dto.PersonDTO;
import com.eni.fleetviewer.back.dto.RoleDTO;
import com.eni.fleetviewer.back.model.AppUser;
import com.eni.fleetviewer.back.model.Person;
import com.eni.fleetviewer.back.repository.AppUserRepository;
import com.eni.fleetviewer.back.service.AuthService;
import com.eni.fleetviewer.back.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/users")   // Path pour l'API
public class AppUserController {

    @Autowired private AppUserRepository userRepository;
    @Autowired private AuthService authService;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<AppUserDTO> getUsers() {    // Renvoie tous les utilisateurs
        return userRepository.findAll().stream()
                .map(AppUserDTO::new)
                .toList();
    }

    @GetMapping("/{username}")
    public AppUserDTO getUserByUsername(@PathVariable String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return new AppUserDTO(user);
    }

    @GetMapping("/current")
    public AppUserDTO getCurrentUser(Principal principal) {
        AppUser user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return new AppUserDTO(user);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentUser(@RequestBody AppUserDTO userDTO, Principal principal) {
        AppUser user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        String token = null;

        // Si on change le username
        boolean usernameChanged = !user.getUsername().equals(userDTO.getUsername());
        if (usernameChanged) {
            // Vérifier que le nouveau username n'existe pas déjà
            Optional<AppUser> userOptional = userRepository.findByUsername(userDTO.getUsername());
            if (userOptional.isPresent()) {
                throw new RuntimeException("Nom d'utilisateur déjà utilisé");
            }
            // Vérifier que le mot de passe envoyé est correct
            if (userDTO.getPassword() == null || !authService.checkPassword(user, userDTO.getPassword())) {
                throw new RuntimeException("Mot de passe incorrect");
            }
            // Si tout est bon, on applique la modification
            user.setUsername(userDTO.getUsername());
        }

        //MAJ des infos personnelles (Person)
        if (userDTO.getPerson() != null) {
            Person p = user.getPerson();
            if (p == null) p = new Person();
            PersonDTO personDTO = userDTO.getPerson();

            p.setFirstName(personDTO.getFirstName());
            p.setLastName(personDTO.getLastName());
            p.setEmail(personDTO.getEmail());
            p.setPhone(personDTO.getPhone());
            if (personDTO.getAddress() != null) p.setAddress(personDTO.getAddress().toEntity());
            if (personDTO.getPlace() != null) p.setPlace(personDTO.getPlace().toEntity());
            user.setPerson(p);
        }

        AppUser savedUser = userRepository.save(user);

        // Génère le token APRÈS le save
        if (usernameChanged) {
            token = jwtService.generateToken(savedUser);
        }

        if (token != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("user", new AppUserDTO(savedUser));
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(new AppUserDTO(savedUser));
        }
    }

    @PostMapping
    public AppUserDTO createUser(@RequestBody AppUserDTO dto) {
        AppUser u = dto.toEntity();
        u.setPassword(passwordEncoder.encode(dto.getPassword()));

        AppUser savedUser = userRepository.save(u);
        return new AppUserDTO(savedUser);
    }

    @PutMapping("/{id}")
    public AppUserDTO updateUser(@PathVariable Long id, @RequestBody AppUserDTO userDTO) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        //

        AppUser savedUser = userRepository.save(user);
        return new AppUserDTO(savedUser);
    }

    @PutMapping("/status/{id}")
    public AppUserDTO updateUserStatus(@PathVariable Long id, @RequestParam boolean enabled) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        user.setEnabled(enabled);
        AppUser savedUser = userRepository.save(user);
        return new AppUserDTO(savedUser);
    }

    @GetMapping("/username")
    public String generateUsername(@RequestParam String firstName, @RequestParam String lastName) {
        String baseUsername = ((!firstName.isEmpty() ? firstName.trim().charAt(0) : "") + (!lastName.isEmpty() ? lastName.trim() : "")).toLowerCase();
        String username = baseUsername;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Si ça existe déjà, ajouter suffixe alphanumérique aléatoire
        while(userRepository.findByUsername(username)
                .filter(user -> !user.getUsername().equals((currentUsername)))
                .isPresent()) {
            String randomSuffix = UUID.randomUUID().toString().substring(0, 3).toLowerCase();
            username = baseUsername + randomSuffix;
        }
        return username;
    }
}
