package com.eni.fleetviewer.back.dto;

import com.eni.fleetviewer.back.model.PlaceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceTypeDTO {
    private Long id;
    private String name;

    public PlaceTypeDTO(PlaceType placeType) {
        this.id = placeType.getId();
        this.name = placeType.getName();
    }

    public PlaceType toEntity() {
        PlaceType pt = new PlaceType();
        pt.setId(id);
        pt.setName(name);
        return pt;
    }
}
