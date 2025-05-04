package com.eni.fleetviewer.back.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "users") //
public class User {

    @Id
    @Column(name = "username")
    private String username;

    @Setter
    private String password;

    @Setter
    private String role;

    @Setter
    private boolean enabled;

    // Constructeur vide obligatoire, Getters, setters géré par Lombok
    public User() {}

    public User(String username, String password, String role, boolean enabled) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

}
