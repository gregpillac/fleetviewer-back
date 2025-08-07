package com.eni.fleetviewer.back.dto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
private Long id;  // identifiant du véhicule (généré par la BDD)

@NotNull
private String licensePlate;    // numéro de plaque d’immatriculation

@NotNull
private String brand;           // marque du véhicule

@NotNull
private String model;           // modèle du véhicule

@NotNull
private Integer seats;          // nombre de places assises

@NotNull
private Long mileage;           // kilométrage actuel

@NotNull
private Boolean isRoadworthy;   // contrôle technique valide ?

@NotNull
private Boolean isInsuranceValid;  // assurance valide ?

@NotNull
private Long placeId;           // référence à l’entité Place (lieu associé)

}