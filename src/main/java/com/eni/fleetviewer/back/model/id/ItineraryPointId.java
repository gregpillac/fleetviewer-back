package com.eni.fleetviewer.back.model.id;


import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ItineraryPointId implements Serializable {

    private Long reservation;
    private Long person;
}
