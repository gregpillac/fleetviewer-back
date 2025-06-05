package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.AppUserDTO;
import com.eni.fleetviewer.back.model.AppUser;
import com.eni.fleetviewer.back.model.Person;
import com.eni.fleetviewer.back.repository.AppUserRepository;
import com.eni.fleetviewer.back.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.List;

@RestController
@RequestMapping("/api/users")   // Path pour l'API
public class AppUserController {

    @Autowired
    private AppUserRepository userRepository;
    private PersonRepository personRepository;

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
    public AppUserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return new AppUserDTO(user);
    }

    @PostMapping
    public AppUserDTO createUser(@RequestBody AppUser user) {
        AppUser savedUser = userRepository.save(user);
        return new AppUserDTO(savedUser);
    }

    @PutMapping("/{id}")
    public AppUserDTO updateUser(@PathVariable Long id, @RequestBody AppUser updatedUser) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        user.setUsername(updatedUser.getUsername());
        user.setRole(updatedUser.getRole());

        if (updatedUser.getPerson() != null) {
            user.setPerson(updatedUser.getPerson());
        }

        AppUser savedUser = userRepository.save(user);
        return new AppUserDTO(savedUser);
    }

    @PutMapping("/disable/{id}")
    public AppUserDTO disableUser(@PathVariable Long id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        user.setEnabled(false);
        AppUser savedUser = userRepository.save(user);
        return new AppUserDTO(savedUser);
    }


}
