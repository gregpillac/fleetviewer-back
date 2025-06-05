package com.eni.fleetviewer.back.dto;

import com.eni.fleetviewer.back.model.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDTO {
    private Long id;
    private String name;
    private boolean isPublic;
    private PlaceTypeDTO placeType;

    public PlaceDTO(Place place) {
        this.id = place.getId();
        this.name = place.getName();
        this.isPublic = place.isPublic();
        if (place.getPlaceType() != null) {
            this.placeType = new PlaceTypeDTO(place.getPlaceType());
        }
    }
}
