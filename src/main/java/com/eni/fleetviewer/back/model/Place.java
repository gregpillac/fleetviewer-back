package com.eni.fleetviewer.back.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "places")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_type_id", nullable = false)
    private PlaceType placeType;

    @OneToOne(
            cascade = CascadeType.ALL,      // PERSIST, MERGE, REMOVE, etc.
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "address_id", nullable = false, unique = true)
    private Address address;

    @OneToMany(mappedBy = "place")
    private Set<VehicleKey> keys = new HashSet<>();
}
