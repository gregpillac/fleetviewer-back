package com.eni.fleetviewer.back.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "app_users")
public class AppUser {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(length = 50)
    private String username;

    @Setter
    private String password;

    @Setter
    @Column(name = "role_id", length = 50)
    private String role;

    @Setter
    private boolean enabled;

    // Constructeur vide obligatoire, Getters, setters géré par Lombok
    public AppUser() {}

    public AppUser(String username, String password, String role, boolean enabled) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

}
