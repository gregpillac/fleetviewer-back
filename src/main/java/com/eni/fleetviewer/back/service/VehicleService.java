package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.dto.VehicleDTO;
import com.eni.fleetviewer.back.mapper.VehicleMapper;
import com.eni.fleetviewer.back.model.Vehicle;
import com.eni.fleetviewer.back.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepo;
    private final VehicleMapper vehicleMapper;

    public VehicleService(VehicleRepository vehicleRepo,
                          VehicleMapper vehicleMapper) {
        this.vehicleRepo = vehicleRepo;
        this.vehicleMapper = vehicleMapper;
    }

    /**
     * Création et persistance d’un nouveau véhicule à partir du DTO.
     */
    @Transactional
    public VehicleDTO addVehicle(VehicleDTO dto) {

        // 1) Conversion du DTO en entité
        Vehicle v = vehicleMapper.toEntity(dto);

        // 2) Sauvegarde en base
        Vehicle saved = vehicleRepo.save(v);

        // 3) Retourne un DTO construit à partir de l’entité persistée
        return vehicleMapper.toDto(saved);
    }
}