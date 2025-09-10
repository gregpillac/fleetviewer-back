package com.eni.fleetviewer.back.model;

import com.eni.fleetviewer.back.model.id.ItineraryPointId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ItineraryPointId.class)
@Table(name = "itinerary_points")
public class ItineraryPoint {

    // Clé primaire composite définie dans la classe ItineraryPointId
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    // Clé primaire composite définie dans la classe ItineraryPointId
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @NotNull
    @Column(name = "date_time")
    private LocalDateTime dateTime;
}
