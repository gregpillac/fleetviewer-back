package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.AppUserDTO;
import com.eni.fleetviewer.back.dto.ChangePasswordDTO;
import com.eni.fleetviewer.back.service.AppUserService;
import com.eni.fleetviewer.back.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;
    private final JwtService jwtService;

    // ---- list ----
    @GetMapping
    public List<AppUserDTO> getUsers() {
        return appUserService.findAll();
    }

    // ---- get by username ----
    @GetMapping("/{username}")
    public AppUserDTO getUserByUsername(@PathVariable String username) {
        return appUserService.findByUsername(username);
    }

    // ---- current (/me) ----
    @GetMapping("/me")
    public AppUserDTO getCurrentUser(Principal principal) {
        return appUserService.currentUser(principal);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentUser(@RequestBody AppUserDTO userDTO, Principal principal) {
        var result = appUserService.updateCurrentUser(userDTO, principal, jwtService);
        if (result.token() != null) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("user", result.user());
            payload.put("token", result.token());
            return ResponseEntity.ok(payload);
        }
        return ResponseEntity.ok(result.user());
    }

    @PutMapping("/me/password")
    @ResponseStatus(HttpStatus.OK)
    public void changeCurrentUserPassword(@RequestBody ChangePasswordDTO dto, Principal principal) {
        appUserService.changeCurrentUserPassword(dto, principal);
    }

    // ---- create ----
    @PostMapping
    public AppUserDTO createUser(@RequestBody AppUserDTO dto) {
        return appUserService.create(dto);
    }

    // ---- update (ADMIN) ----
    @PutMapping("/{id}")
    public AppUserDTO updateUser(@PathVariable Long id, @RequestBody AppUserDTO userDTO) {
        return appUserService.update(id, userDTO);
    }

    // ---- update status ----
    @PutMapping("/status/{id}")
    public AppUserDTO updateUserStatus(@PathVariable Long id, @RequestParam boolean enabled) {
        return appUserService.updateStatus(id, enabled);
    }

    // ---- delete ----
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        appUserService.delete(id);
    }

    // ---- generate username ----
    @GetMapping("/generate-username")
    public String generateUsername(@RequestParam String firstName,
                                   @RequestParam String lastName,
                                   Authentication authentication) {
        return appUserService.generateUsername(firstName, lastName, authentication);
    }
}
