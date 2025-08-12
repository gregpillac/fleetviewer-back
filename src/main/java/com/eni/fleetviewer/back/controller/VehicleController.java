package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.VehicleDTO;
import com.eni.fleetviewer.back.model.Place;
import com.eni.fleetviewer.back.model.Vehicle;
import com.eni.fleetviewer.back.repository.PlaceRepository;
import com.eni.fleetviewer.back.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @GetMapping
    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream().map(VehicleDTO::new).toList();
    }

    @GetMapping("/{id}")
    public VehicleDTO getVehicleById(@PathVariable Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));
        return new VehicleDTO(vehicle);
    }

    @PostMapping
    public VehicleDTO createVehicle(@RequestBody Vehicle vehicle) {
        // Vérifier que le place existe
        Place place = placeRepository.findById(vehicle.getPlace().getId())
                .orElseThrow(() -> new RuntimeException("Lieu non trouvé"));
        vehicle.setPlace(place);
        return new VehicleDTO(vehicleRepository.save(vehicle));
    }

    @PutMapping("/{id}")
    public VehicleDTO updateVehicle(@PathVariable Long id, @RequestBody Vehicle updated) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));

        vehicle.setLicensePlate(updated.getLicensePlate());
        vehicle.setBrand(updated.getBrand());
        vehicle.setModel(updated.getModel());
        vehicle.setSeats(updated.getSeats());
        vehicle.setMileage(updated.getMileage());
        vehicle.setRoadworthy(updated.isRoadworthy());
        vehicle.setInsuranceValid(updated.isInsuranceValid());

        if (updated.getPlace() != null) {
            Place place = placeRepository.findById(updated.getPlace().getId())
                    .orElseThrow(() -> new RuntimeException("Lieu non trouvé"));
            vehicle.setPlace(place);
        }

        return new VehicleDTO(vehicleRepository.save(vehicle));
    }

    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable Long id) {
        vehicleRepository.deleteById(id);
    }
}
