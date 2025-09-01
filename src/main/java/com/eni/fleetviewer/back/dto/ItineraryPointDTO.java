package com.eni.fleetviewer.back.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryPointDTO {

    private Long reservationId;     // ID de la réservation (clé composite)
    private Long placeId;           // ID du lieu (clé composite)
    private LocalDateTime dateTime; // Date et heure du point d’itinéraire
    private String pointType;       // Type (départ, arrivée, etc.)

}