package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.dto.ReservationStatusDTO;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import com.eni.fleetviewer.back.mapper.ReservationStatusMapper;
import com.eni.fleetviewer.back.repository.ReservationStatusRepository;
import com.eni.fleetviewer.back.model.ReservationStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationStatusService {

    private final ReservationStatusRepository repository;
    private final ReservationStatusMapper mapper;

    public ReservationStatusService(ReservationStatusRepository repository, ReservationStatusMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ReservationStatusDTO> getAllStatuses() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public ReservationStatusDTO getStatusById(Long id) {
        ReservationStatus status = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Statut introuvable pour l'ID " + id));
        return mapper.toDto(status);
    }
}