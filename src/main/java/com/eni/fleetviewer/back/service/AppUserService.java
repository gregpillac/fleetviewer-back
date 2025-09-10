package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.dto.AppUserDTO;
import com.eni.fleetviewer.back.dto.ChangePasswordDTO;
import com.eni.fleetviewer.back.dto.PersonDTO;
import com.eni.fleetviewer.back.dto.PlaceDTO;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import com.eni.fleetviewer.back.mapper.AddressMapper;
import com.eni.fleetviewer.back.mapper.PlaceMapper;
import com.eni.fleetviewer.back.mapper.UserMapper;
import com.eni.fleetviewer.back.model.*;
import com.eni.fleetviewer.back.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserService {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;
    private final PlaceRepository placeRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    // --------- Queries ---------

    @Transactional(readOnly = true)
    public List<AppUserDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public AppUserDTO findByUsername(String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public AppUserDTO findByPersonId(Long id) {
        return userRepository.findByPersonId(id)
                .map(userMapper::toDto)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public AppUserDTO currentUser(Principal principal) {
        AppUser user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
        return userMapper.toDto(user);
    }

    // --------- Commands ---------

    public AppUserDTO create(AppUserDTO dto) {
        // map basique
        AppUser entity = userMapper.toEntity(dto);

        // password requis
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mot de passe requis");
        }
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));

        // sécuriser le rôle (référence par id)
        if (dto.getRole() != null && dto.getRole().getId() != null) {
            Role ref = roleRepository.getReferenceById(dto.getRole().getId());
            entity.setRole(ref);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rôle requis");
        }

        // sécuriser l'association Person : on attend une Person existante (id)
        if (dto.getPerson() == null || dto.getPerson().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Person id requis");
        } else {
            Person pRef = personRepository.getReferenceById(dto.getPerson().getId());
            entity.setPerson(pRef);
        }

        // username unique
        if (userRepository.findByUsername(entity.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Nom d'utilisateur déjà utilisé");
        }

        AppUser saved = userRepository.save(entity);
        return userMapper.toDto(saved);
    }

    public AppUserDTO update(Long id, AppUserDTO dto) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));

        // username (si changé, vérifier unicité)
        if (dto.getUsername() != null && !dto.getUsername().isBlank()
                && !dto.getUsername().equals(user.getUsername())) {
            boolean taken = userRepository.findByUsername(dto.getUsername())
                    .filter(u -> !u.getId().equals(id))
                    .isPresent();
            if (taken) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Nom d'utilisateur déjà utilisé");
            }
            user.setUsername(dto.getUsername());
        }

        user.setEnabled(dto.isEnabled());

        // rôle (optionnel) — ne passe que par id
        if (dto.getRole() != null && dto.getRole().getId() != null) {
            Role ref = roleRepository.getReferenceById(dto.getRole().getId());
            user.setRole(ref);
        }

        // password (optionnel) — seulement si non vide
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // ⚠️ On NE touche PAS à user.setPerson(...) ici (gérer via endpoints Person)
        AppUser saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    public AppUserDTO updateStatus(Long id, boolean enabled) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));
        user.setEnabled(enabled);
        AppUser saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    // --------- /me ---------

    /** Retourne (userDTO, token) ; token non nul seulement si le username a changé */
    public record UserAndToken(AppUserDTO user, String token) {}

    public UserAndToken updateCurrentUser(AppUserDTO dto, Principal principal, JwtService jwtService) {
        AppUser user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));

        String newToken = null;

        // 1) Changement de username (sécurisé)
        boolean usernameChanged = false;
        if (dto.getUsername() != null && !dto.getUsername().isBlank()
                && !dto.getUsername().equals(user.getUsername())) {

            // unicité
            Optional<AppUser> existing = userRepository.findByUsername(dto.getUsername());
            if (existing.isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Nom d'utilisateur déjà utilisé");
            }

            // mot de passe courant requis pour autoriser le changement
            if (dto.getPassword() == null || dto.getPassword().isBlank() ||
                    !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Mot de passe incorrect");
            }

            user.setUsername(dto.getUsername());
            usernameChanged = true;
        }

        // 2) MAJ infos Person (facultatif)
        if (dto.getPerson() != null) {
            Person p = Optional.ofNullable(user.getPerson()).orElseGet(Person::new);
            PersonDTO pdto = dto.getPerson();

            p.setFirstName(pdto.getFirstName());
            p.setLastName(pdto.getLastName());
            p.setEmail(pdto.getEmail());
            p.setPhone(pdto.getPhone());

            // ----- Address (clé du bug) -----
            if (pdto.getAddress() != null) {
                var ad = pdto.getAddress();

                Address managedAddr;
                if (ad.getId() != null) {
                    managedAddr = addressRepository.findById(ad.getId())
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "Adresse introuvable: " + ad.getId()));
                } else {
                    managedAddr = new Address();
                }

                managedAddr.setAddressFirstLine(ad.getAddressFirstLine());
                managedAddr.setAddressSecondLine(ad.getAddressSecondLine());
                managedAddr.setPostalCode(ad.getPostalCode());
                managedAddr.setCity(ad.getCity());

                // assure un id si nouvelle adresse
                managedAddr = addressRepository.save(managedAddr);

                p.setAddress(managedAddr);
            } else {
                p.setAddress(null);
            }

            // ----- Place : référence managée uniquement -----
            if (pdto.getPlace() != null && pdto.getPlace().getId() != null) {
                p.setPlace(placeRepository.getReferenceById(pdto.getPlace().getId()));
            } else {
                p.setPlace(null);
            }

            user.setPerson(p);
        }

        // 3) enabled/role/password
        user.setEnabled(dto.isEnabled());
        if (dto.getRole() != null && dto.getRole().getId() != null) {
            user.setRole(roleRepository.getReferenceById(dto.getRole().getId()));
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank() && !usernameChanged) {
            // Sur /me, si ce champ est utilisé pour "nouveau mot de passe", fais plutôt un endpoint dédié (déjà présent).
            // Ici, on n'encode que si tu décides de l'autoriser dans ce endpoint.
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        AppUser saved = userRepository.save(user);

        if (usernameChanged) {
            newToken = jwtService.generateToken(saved);
        }

        return new UserAndToken(userMapper.toDto(saved), newToken);
    }

    public void changeCurrentUserPassword(ChangePasswordDTO dto, Principal principal) {
        AppUser user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));

        if (dto.getCurrentPassword() == null || dto.getNewPassword() == null ||
                dto.getCurrentPassword().isBlank() || dto.getNewPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tous les champs sont obligatoires");
        }

        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le nouveau mot de passe doit être différent de l'actuel");
        }

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Mot de passe actuel incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    // --------- Utilitaire ---------

    public String generateUsername(String firstName, String lastName, Authentication auth) {
        String base = ((firstName != null && !firstName.isBlank() ? firstName.trim().substring(0,1) : "")
                + (lastName != null ? lastName.trim() : "")).toLowerCase();

        String currentUsername = auth != null ? auth.getName() : null;

        String candidate = base;
        while (userRepository.findByUsername(candidate)
                .filter(u -> currentUsername == null || !u.getUsername().equals(currentUsername))
                .isPresent()) {
            String suffix = UUID.randomUUID().toString().substring(0, 3).toLowerCase();
            candidate = base + suffix;
        }
        return candidate;
    }
}
