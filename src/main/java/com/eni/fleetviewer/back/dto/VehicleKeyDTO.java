package com.eni.fleetviewer.back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleKeyDTO {
    private Long id;
    private String tagLabel;
    private Long vehicleId;
    private Long placeId;
}
