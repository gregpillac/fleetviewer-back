package com.eni.fleetviewer.back.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @Column(name = "authority_id")
    private String id; // Nom de la permission

    @Column(name = "description")
    private String description;
}
