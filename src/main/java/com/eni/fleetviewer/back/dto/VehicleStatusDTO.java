package com.eni.fleetviewer.back.dto;

import com.eni.fleetviewer.back.model.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleStatusDTO {
    private Long id;
    private String name;

    public VehicleStatusDTO(VehicleStatus status) {
        this.id = status.getId();
        this.name = status.getName();
    }
}
