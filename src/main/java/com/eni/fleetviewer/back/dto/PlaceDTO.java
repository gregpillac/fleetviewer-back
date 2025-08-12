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
public class PlaceDTO {

    private Long id;            // identifiant du Site (généré par la BDD)

    @NotNull
    private String name;        // nom du Site

    @NotNull
    private Long placeId;       // référence à l’entité Place (lieu associé)

    @NotNull
    private boolean isPublic;   // ????????????

    @NotNull
    private Long placeTypeId;   // référence à l’entité PlaceType (type de lieu associé)

    private Long addressId;     // référence à l’entité Address (adresse associée)

    private Long createdById;   // "Lieux créé par .." (optionnel, si nécessaire)
}