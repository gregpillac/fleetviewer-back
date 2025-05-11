package com.eni.fleetviewer.back.model.id;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class FavoritePlaceId implements Serializable {

    private Long person;
    private Long place;
}
