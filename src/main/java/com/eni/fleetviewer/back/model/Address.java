package com.eni.fleetviewer.back.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @NotNull
    @Column(name = "address_line_1", nullable = false)
    private String addressFirstLine;

    @Column(name = "address_line_2")
    private String addressSecondLine;

    @NotNull
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @NotNull
    @Column(nullable = false)
    private String city;

    @Column(name = "gps_coords")
    private String gpsCoords;
}
