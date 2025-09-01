package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.dto.VehicleStatusDTO;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import com.eni.fleetviewer.back.mapper.VehicleStatusMapper;
import com.eni.fleetviewer.back.model.VehicleStatus;
import com.eni.fleetviewer.back.repository.VehicleStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleStatusService {

    private final VehicleStatusRepository repository;
    private final VehicleStatusMapper mapper;

    @Autowired
    public VehicleStatusService(VehicleStatusRepository repository, VehicleStatusMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<VehicleStatusDTO> getAllStatuses() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VehicleStatusDTO getById(Long id) {
        VehicleStatus status = repository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Statut de véhicule introuvable pour l'ID " + id));
        return mapper.toDto(status);
    }
}