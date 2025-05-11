package com.eni.fleetviewer.back.model.id;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RideshareId implements Serializable {
    private Long person;
    private Long reservation;
}
