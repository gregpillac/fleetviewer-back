package com.eni.fleetviewer.back.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
