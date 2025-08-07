package com.eni.fleetviewer.back.service;

import com.eni.fleetviewer.back.dto.VehicleDTO;
import com.eni.fleetviewer.back.model.Place;
import com.eni.fleetviewer.back.model.Vehicle;
import com.eni.fleetviewer.back.repository.PlaceRepository;
import com.eni.fleetviewer.back.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepo;
    private final PlaceRepository placeRepo;

    public VehicleService(VehicleRepository vehicleRepo,
                          PlaceRepository placeRepo) {
        this.vehicleRepo = vehicleRepo;
        this.placeRepo = placeRepo;
    }

    /**
     * Création et persistance d’un nouveau véhicule à partir du DTO.
     */
    @Transactional
    public VehicleDTO addVehicle(VehicleDTO dto) {
        // 1) Récupère la Place associée ou lève une exception si introuvable
        Place place = placeRepo.findById(dto.getPlaceId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Place introuvable pour l’ID " + dto.getPlaceId()));

        // 2) Bind des champs du DTO dans une nouvelle entité Vehicle
        Vehicle v = new Vehicle();
        v.setLicensePlate(dto.getLicensePlate());
        v.setBrand(dto.getBrand());
        v.setModel(dto.getModel());
        v.setSeats(dto.getSeats());
        v.setMileage(dto.getMileage());
        v.setRoadworthy(dto.getIsRoadworthy());
        v.setInsuranceValid(dto.getIsInsuranceValid());
        v.setPlace(place);

        // 3) Sauvegarde en base
        Vehicle saved = vehicleRepo.save(v);

        // 4) Retourne un DTO construit à partir de l’entité persistée
        return toDto(saved);
    }

    /**
     * Mapping entité → DTO.
     */
    private VehicleDTO toDto(Vehicle v) {
        return new VehicleDTO(
                v.getId(),
                v.getLicensePlate(),
                v.getBrand(),
                v.getModel(),
                v.getSeats(),
                v.getMileage(),
                v.isRoadworthy(),
                v.isInsuranceValid(),
                v.getPlace().getId()
        );
    }
}