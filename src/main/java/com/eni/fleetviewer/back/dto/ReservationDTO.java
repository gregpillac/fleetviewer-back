package com.eni.fleetviewer.back.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private Long id;

    @NotNull
    @FutureOrPresent(message = "La date de début ne peut pas être dans le passé.")
    private java.time.LocalDateTime startDate;

    @NotNull
    @FutureOrPresent(message = "La date de fin ne peut pas être dans le passé.")
    private java.time.LocalDateTime endDate;

    @NotNull
    private Long reservationStatusId;

    @NotNull
    private Long vehicleId;

    @NotNull
    private Long driverId;

    @NotEmpty
    @Size(min = 2, message = "Une réservation doit contenir au moins un point de départ et un point d'arrivée.")
    private List<ItineraryPointDTO> itineraryPoints;

}
