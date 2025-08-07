package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.VehicleDTO;
import com.eni.fleetviewer.back.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /**
     * POST /api/vehicles
     * Crée un nouveau véhicule et renvoie 201 Created + DTO.
     */
    @PostMapping
    public ResponseEntity<VehicleDTO> addVehicle(
            @Valid @RequestBody VehicleDTO dto) {

        VehicleDTO created = vehicleService.addVehicle(dto);

        // Location header: /api/vehicles/{id}
        URI location = URI.create("/api/vehicles/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }
}