package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.AppUserDTO;
import com.eni.fleetviewer.back.model.AppUser;
import com.eni.fleetviewer.back.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.List;

@RestController
@RequestMapping("/api/users")   // Path pour l'API
public class AppUserController {

    @Autowired
    private AppUserRepository userRepository;

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


}
