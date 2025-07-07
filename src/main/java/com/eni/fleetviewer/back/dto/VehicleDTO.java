package com.eni.fleetviewer.back.dto;

import com.eni.fleetviewer.back.model.Place;
import com.eni.fleetviewer.back.model.Vehicle;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {

    private Long id;
    private String licensePlate;
    private String brand;
    private String model;
    private Integer seats;
    private Long mileage;
    private boolean isRoadworthy;
    private boolean isInsuranceValid;
    private PlaceDTO place;

    public VehicleDTO(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.licensePlate = vehicle.getLicensePlate();
        this.brand = vehicle.getBrand();
        this.model = vehicle.getModel();
        this.seats = vehicle.getSeats();
        this.mileage = vehicle.getMileage();
        this.isRoadworthy = vehicle.isRoadworthy();
        this.isInsuranceValid = vehicle.isInsuranceValid();
        if (vehicle.getPlace() != null) {
            this.place = new PlaceDTO(vehicle.getPlace());
        }
    }
}
