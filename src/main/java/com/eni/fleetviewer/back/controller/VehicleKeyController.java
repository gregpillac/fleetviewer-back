package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.VehicleKeyDTO;
import com.eni.fleetviewer.back.service.VehicleKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle-keys")
@RequiredArgsConstructor
public class VehicleKeyController {

    private final VehicleKeyService service;

    @GetMapping
    public ResponseEntity<List<VehicleKeyDTO>> getAllKeys() {
        List<VehicleKeyDTO> keys = service.getAllKeys();
        return ResponseEntity.ok(keys);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleKeyDTO> get(@PathVariable Long id) {
        VehicleKeyDTO dto = service.get(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<VehicleKeyDTO> create(@Valid @RequestBody VehicleKeyDTO dto) {
        VehicleKeyDTO created = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleKeyDTO> update(@PathVariable Long id, @Valid @RequestBody VehicleKeyDTO dto) {
        VehicleKeyDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Filtres
    @GetMapping("/by-vehicle/{vehicleId}")
    public ResponseEntity<List<VehicleKeyDTO>> getByVehicle(@PathVariable Long vehicleId) {
        List<VehicleKeyDTO> list = service.getByVehicle(vehicleId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/by-place/{placeId}")
    public ResponseEntity<List<VehicleKeyDTO>> getByPlace(@PathVariable Long placeId) {
        List<VehicleKeyDTO> list = service.getByPlace(placeId);
        return ResponseEntity.ok(list);
    }
}
