package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.dto.ReservationStatusDTO;
import com.eni.fleetviewer.back.mapper.ReservationStatusMapper;
import com.eni.fleetviewer.back.repository.ReservationStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationStatusService {

    private final ReservationStatusRepository reservationStatusRepository;
    private final ReservationStatusMapper reservationStatusMapper;

    public ReservationStatusService(ReservationStatusRepository reservationStatusRepository,
                                    ReservationStatusMapper reservationStatusMapper) {
        this.reservationStatusRepository = reservationStatusRepository;
        this.reservationStatusMapper = reservationStatusMapper;
    }

    public List<ReservationStatusDTO> getAllStatuses() {
        return reservationStatusRepository.findAll()
                .stream()
                .map(reservationStatusMapper::toDto)
                .collect(Collectors.toList());
    }
}