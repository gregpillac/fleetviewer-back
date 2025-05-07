package com.eni.fleetviewer.back.model;

import com.eni.fleetviewer.back.model.id.RideshareId;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(RideshareId.class)
@Table(name = "rideshare")
public class Rideshare {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}
