package com.eni.fleetviewer.back.controller;

import com.eni.fleetviewer.back.dto.ReservationStatusDTO;
import com.eni.fleetviewer.back.service.ReservationStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservationStatus")
public class ReservationStatusController {

    private final ReservationStatusService reservationStatusService;

    @Autowired
    public ReservationStatusController(ReservationStatusService reservationStatusService) {
        this.reservationStatusService = reservationStatusService;
    }

    /**
     * GET /api/reservationStatus
     */
    @GetMapping
    public ResponseEntity<List<ReservationStatusDTO>> getAllStatuses() {
        List<ReservationStatusDTO> statuses = reservationStatusService.getAllStatuses();
        return ResponseEntity.ok(statuses);
    }
}