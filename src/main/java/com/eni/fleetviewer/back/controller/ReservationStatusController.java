package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.ReservationStatusDTO;
import com.eni.fleetviewer.back.service.ReservationStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservationStatus")
public class ReservationStatusController {

    private final ReservationStatusService reservationStatusService;

    public ReservationStatusController(ReservationStatusService reservationStatusService) {
        this.reservationStatusService = reservationStatusService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationStatusDTO>> getAllStatuses() {
        return ResponseEntity.ok(reservationStatusService.getAllStatuses());
    }
}