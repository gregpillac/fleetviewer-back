package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.AppUserDTO;
import com.eni.fleetviewer.back.dto.ChangePasswordDTO;
import com.eni.fleetviewer.back.dto.PersonDTO;
import com.eni.fleetviewer.back.mapper.AddressMapper;
import com.eni.fleetviewer.back.mapper.PlaceMapper;
import com.eni.fleetviewer.back.mapper.UserMapper;
import com.eni.fleetviewer.back.model.AppUser;
import com.eni.fleetviewer.back.model.Person;
import com.eni.fleetviewer.back.repository.AppUserRepository;
import com.eni.fleetviewer.back.service.AuthService;
import com.eni.fleetviewer.back.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;


import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/users")   // Path pour l'API
public class AppUserController {

    @Autowired private AppUserRepository userRepository;
    @Autowired private AuthService authService;
    @Autowired private JwtService jwtService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private PlaceMapper placeMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private AddressMapper addressMapper;

    @GetMapping
    public List<AppUserDTO> getUsers() {    // Renvoie tous les utilisateurs
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/{username}")
    public AppUserDTO getUserByUsername(@PathVariable String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return userMapper.toDto(user);
    }

    @GetMapping("/me")
    public AppUserDTO getCurrentUser(Principal principal) {
        AppUser user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return userMapper.toDto(user);
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
            if (personDTO.getAddress() != null) p.setAddress(addressMapper.toEntity(personDTO.getAddress()));
            if (personDTO.getPlace() != null) p.setPlace(placeMapper.toEntity(personDTO.getPlace()));
            user.setPerson(p);
        }

        AppUser savedUser = userRepository.save(user);

        // Génère le token APRÈS le save
        if (usernameChanged) {
            token = jwtService.generateToken(savedUser);
        }

        if (token != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("user", userMapper.toDto(savedUser));
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(userMapper.toDto(savedUser));
        }
    }

    @PutMapping("/me/password")
    public ResponseEntity<?> changeCurrentUserPassword(
            @RequestBody @Validated ChangePasswordDTO dto,
            Principal principal) {

        AppUser user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));

        // Vérifier présence des champs
        if (dto.getCurrentPassword() == null || dto.getNewPassword() == null ||
                dto.getCurrentPassword().isBlank() || dto.getNewPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tous les champs sont obligatoires");
        }

        // Vérifier que le nouveau mot de passe n'est pas le même que l'actuel
        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le nouveau mot de passe doit être différent de l'actuel");
        }

        // Vérifier l'ancien mot de passe
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Mot de passe actuel incorrect");
        }

        // Encode et set le nouveau mot de passe
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public AppUserDTO createUser(@RequestBody AppUserDTO dto) {
        AppUser u = userMapper.toEntity(dto);
        u.setPassword(passwordEncoder.encode(dto.getPassword()));

        AppUser savedUser = userRepository.save(u);
        return userMapper.toDto(savedUser);
    }

    @PutMapping("/{id}")
    public AppUserDTO updateUser(@PathVariable Long id, @RequestBody AppUserDTO userDTO) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        AppUser savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @PutMapping("/status/{id}")
    public AppUserDTO updateUserStatus(@PathVariable Long id, @RequestParam boolean enabled) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        user.setEnabled(enabled);
        AppUser savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @GetMapping("/generate-username")
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
