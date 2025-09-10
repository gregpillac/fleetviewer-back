package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.dto.PersonDTO;
import com.eni.fleetviewer.back.dto.VehicleDTO;
import com.eni.fleetviewer.back.mapper.IdToEntityMapper;
import com.eni.fleetviewer.back.mapper.VehicleMapper;
import com.eni.fleetviewer.back.model.Vehicle;
import com.eni.fleetviewer.back.repository.VehicleRepository;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepo;
    private final VehicleMapper vehicleMapper;
    private final IdToEntityMapper idToEntityMapper;

    /**
     * Récupération de tous les véhicules.
     * @return la liste des DTO de véhicules
     */
    @Transactional(readOnly = true)
    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepo.findAll()
                .stream()
                .map(vehicleMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupération d’un véhicule par son identifiant.
     * @param id l'identifiant du véhicule
     * @return le DTO du véhicule correspondant
     */
    @Transactional(readOnly = true)
    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepo.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Véhicule non trouvé pour l'id " + id));
        return vehicleMapper.toDto(vehicle);
    }

    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesByPlaceName(String placeName) {
        return vehicleRepo.findByPlaceName(placeName)
                .stream()
                .map(vehicleMapper::toDto)
                .toList();
    }

    /**
     * Suppression d’un véhicule par son identifiant.
     * @param id l'identifiant du véhicule à supprimer
     */
    @Transactional
    public void deleteVehicleById(Long id) {
        if (!vehicleRepo.existsById(id)) {
            throw new RessourceNotFoundException("Véhicule non trouvé pour l'id " + id);
        }
        vehicleRepo.deleteById(id);
    }

    /**
     * Création et persistance d’un nouveau véhicule à partir du DTO.
     * @param dto le DTO à convertir en entité et à persister
     * @return le DTO du véhicule créé
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

    /**
     * Mise à jour d’un véhicule existant.
     * @param id l'identifiant du véhicule à mettre à jour
     * @param dto le DTO contenant les nouvelles informations
     * @return le DTO du véhicule mis à jour
     */
    @Transactional
    public VehicleDTO updateVehicle(Long id, VehicleDTO dto) {
        Vehicle existing = vehicleRepo.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Véhicule non trouvé pour l'id " + id));
        // Mise à jour les champs
        existing.setSeats(dto.getSeats());
        existing.setMileage(dto.getMileage());
        existing.setIsRoadworthy(dto.getIsRoadworthy());
        existing.setIsInsuranceValid(dto.getIsInsuranceValid());
        existing.setPlace(idToEntityMapper.longToPlace(dto.getPlaceId()));
        // Sauvegarde
        Vehicle saved = vehicleRepo.save(existing);
        return vehicleMapper.toDto(saved);
    }

    /**
     * Récupération des véhicules par placeId.
     * @return la liste des DTO de véhicules
     */
    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesByPlaceId(Long id) {
        return vehicleRepo.findByPlaceId(id)
                .stream()
                .map(vehicleMapper::toDto)
                .toList();
    }
}