package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.ItineraryPointDTO;
import com.eni.fleetviewer.back.service.ItineraryPointService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer les points d'itinéraire.
 */
@RestController
@RequestMapping("/api/itinerary-points")
public class ItineraryPointController {

    private final ItineraryPointService itineraryPointService;

    @Autowired
    public ItineraryPointController(ItineraryPointService itineraryPointService) {
        this.itineraryPointService = itineraryPointService;
    }

    /**
     * Récupère tous les points d'itinéraire.
     */
    @GetMapping
    public ResponseEntity<List<ItineraryPointDTO>> getAll() {
        return ResponseEntity.ok(itineraryPointService.getAll());
    }

    /**
     * Récupère un point d'itinéraire par ID composite.
     */
    @GetMapping("/{reservationId}/{placeId}")
    public ResponseEntity<ItineraryPointDTO> getById(
            @PathVariable Long reservationId,
            @PathVariable Long placeId) {
        return ResponseEntity.ok(itineraryPointService.getById(reservationId, placeId));
    }

    /**
     * Crée un nouveau point d'itinéraire.
     */
    @PostMapping
    public ResponseEntity<ItineraryPointDTO> create(@Valid @RequestBody ItineraryPointDTO dto) {
        ItineraryPointDTO created = itineraryPointService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{reservationId}/{placeId}")
    public ResponseEntity<ItineraryPointDTO> update(
            @PathVariable Long reservationId,
            @PathVariable Long placeId,
            @Valid @RequestBody ItineraryPointDTO dto
    ) {
        ItineraryPointDTO updated = itineraryPointService.update(reservationId, placeId, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Supprime un point d'itinéraire.
     */
    @DeleteMapping("/{reservationId}/{placeId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long reservationId,
            @PathVariable Long placeId) {
        itineraryPointService.deleteItineraryPoint(reservationId, placeId);
        return ResponseEntity.noContent().build();
    }
}