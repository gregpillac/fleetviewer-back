package com.eni.fleetviewer.back.enums;

import lombok.Getter;

@Getter
public enum Status {
    PENDING,       // En attente de validation
    CONFIRMED,     // Confirmée
    REJECTED,     // Rejetée
    CANCELLED,     // Annulée
    UNAVAILABLE   // Indisponible

}
