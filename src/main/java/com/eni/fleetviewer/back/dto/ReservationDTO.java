package com.eni.fleetviewer.back.dto;

import com.eni.fleetviewer.back.enums.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
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
    private Long departureId;

    @NotNull
    private Long arrivalId;

    @NotNull
    @FutureOrPresent(message = "La date de début ne peut pas être dans le passé.")
    private java.time.LocalDateTime startDate;

    @NotNull
    @FutureOrPresent(message = "La date de fin ne peut pas être dans le passé.")
    private java.time.LocalDateTime endDate;

    @NotNull
    private Status reservationStatus;

    private Long vehicleId;

    @NotNull
    private Long driverId;

    private List<ItineraryPointDTO> itineraryPoints;

}
