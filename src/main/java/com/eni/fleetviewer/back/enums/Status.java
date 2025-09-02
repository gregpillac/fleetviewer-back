package com.eni.fleetviewer.back.enums;

import lombok.Getter;

@Getter
public enum Status {

    PENDING("PENDING"),         // En attente de validation
    CONFIRMED("CONFIRMED"),     // Confirmée
    CANCELLED("CANCELLED"),     // Annulée
    UNAVAILABLE("UNAVAILABLE"); // Indisponible

    private final String status;

    Status(String status) {
        this.status = status;
    }
}
