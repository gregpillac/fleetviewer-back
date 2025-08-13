package com.eni.fleetviewer.back.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long id;

    @NotNull
    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @NotNull
    @Column(name = "brand", nullable = false)
    private String brand;

    @NotNull
    @Column(name = "model", nullable = false)
    private String model;

    @NotNull
    @Column(name = "seats", nullable = false)
    private Integer seats;

    @NotNull
    @Column(name = "mileage", nullable = false)
    private Long mileage;

    @NotNull
    @Column(name = "is_roadworthy", nullable = false)
    private Boolean isRoadworthy; // Contrôle technique valable

    @NotNull
    @Column(name = "is_insurance_valid", nullable = false)
    private Boolean isInsuranceValid;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;
}
