package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.VehicleStatusDTO;
import com.eni.fleetviewer.back.service.VehicleStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicleStatus")
public class VehicleStatusController {

    private final VehicleStatusService service;

    @Autowired
    public VehicleStatusController(VehicleStatusService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<VehicleStatusDTO>> getAll() {
        return ResponseEntity.ok(service.getAllStatuses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleStatusDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}