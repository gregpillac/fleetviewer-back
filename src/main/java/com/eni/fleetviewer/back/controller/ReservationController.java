package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.ReservationDTO;
import com.eni.fleetviewer.back.dto.VehicleDTO;
import com.eni.fleetviewer.back.dto.VehicleDTO;
import com.eni.fleetviewer.back.enums.Status;
import com.eni.fleetviewer.back.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eni.fleetviewer.back.enums.Status;
import org.springframework.format.annotation.DateTimeFormat;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        ReservationDTO reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/by-driver/{driverId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByDriver(@PathVariable Long driverId) {
        List<ReservationDTO> reservations = reservationService.getReservationsByDriverId(driverId);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@Valid @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO createdReservation = reservationService.createReservation(reservationDTO);
        URI location = URI.create("/api/reservations/" + createdReservation.getId());
        return ResponseEntity.created(location).body(createdReservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Long id, @Valid @RequestBody ReservationDTO reservationDTO) {
        ReservationDTO updatedReservation = reservationService.updateReservation(id, reservationDTO);
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/compatibles")
    public ResponseEntity<List<ReservationDTO>> getCompatibleReservations(@Valid @RequestBody ReservationDTO reservationDTO) {
        //TODO: ajouter la requete de verification au repository
        List<ReservationDTO> compatibleReservations = reservationService.getCompatibleReservations(reservationDTO);
        return ResponseEntity.ok(compatibleReservations);
    }

    // /api/reservations/by-status?status=PENDING[&placeId=123]
    @GetMapping("/by-status")
    public ResponseEntity<List<ReservationDTO>> getByStatus(
            @RequestParam Status status,
            @RequestParam(required = false) Long placeId) {
        return ResponseEntity.ok(reservationService.getByStatus(status, placeId));
    }

    // /api/reservations/{id}/status?status=CONFIRMED[&vehicleId=7]
    @PutMapping("/{id}/status")
    public ResponseEntity<ReservationDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam Status status,
            @RequestParam(required = false) Long vehicleId) {
        return ResponseEntity.ok(reservationService.updateStatus(id, status, vehicleId));
    }

    // GET /api/reservations/available-vehicles?start=...&end=...[&placeId=...]
    @GetMapping("/available-vehicles")
    public ResponseEntity<List<VehicleDTO>> getAvailable(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) Long placeId) {
        return ResponseEntity.ok(reservationService.getAvailableVehicles(start, end, placeId));
    }



}
