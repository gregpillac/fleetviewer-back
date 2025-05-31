package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.model.AppUser;
import com.eni.fleetviewer.back.repository.AppUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public CustomUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Récupérer l'utilisateur depuis le repository
        AppUser applicationUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + username));

        /**
         * Créer une liste d'autorités à partir du rôle de l'utilisateur
         * Dans Spring Security, les autorités sont les permissions granulaires
         * Les rôles sont généralement préfixés par "ROLE_", mais comme la BD a déjà ce préfixe, on l'utilise directement comme une autorité
         *  TODO: implementer une méthode pour préfixer avec ROLE_ ou desactiver cette contrainte pour ne pas contraindre la BDD
         * Ici, on utilise le rôle de l'utilisateur pour créer une autorité
         * Si on a plusieurs rôles ou autorités, on peut les ajouter à la liste
         *  TODO: Créez les autorités / rôles correctement
         */
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(applicationUser.getRole()) // Utilisation du rôle stocké en BDD
                // TODO: Recuperer via JPA les autorithies associées aux roles dans un 2nd temps
        );

        // Créer et retourner un objet UserDetails de Spring Security
        return new User(
            applicationUser.getUsername(),  // Nom d'utilisateur
            applicationUser.getPassword(),  // Mot de passe hashé
            applicationUser.isEnabled(),    // Compte activé
                true,                       // Compte non expiré
                true,                       // Identifiants non expirés
                true,                       // Compte non verrouillé
                authorities                 // Autorités/rôles
        );
    }
}