package com.eni.fleetviewer.back.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryPointDTO {

    @NotNull
    private Long reservationId;     // ID de la réservation (clé composite), null au moment de sa création

    @NotNull
    private Long personId;          // ID du covoitureur (clé composite), null au moment de sa création

    private Long placeId;           // ID du lieu (clé composite)

    private LocalDateTime dateTime; // Date et heure du rdv (si different de la date de reservation)
}