package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.VehicleDTO;
import com.eni.fleetviewer.back.model.Place;
import com.eni.fleetviewer.back.model.Vehicle;
import com.eni.fleetviewer.back.repository.PlaceRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class VehicleMapper {

    /**
     * Injection du repository Place pour la conversion d'ID en entité Place
     * MapStruct ne gère pas l’injection de dépendances via le constructeur pour les mappers abstraits.
     * MapStruct génère une implémentation concrète à la compilation.
     * La classe doit donc être abstraite pour permettre cette génération.
     */
    @Autowired
    protected PlaceRepository placeRepository;

    @Mappings({
            @Mapping(source = "roadworthy", target = "isRoadworthy"),
            @Mapping(source = "insuranceValid", target = "isInsuranceValid"),
            @Mapping(source = "place.id", target = "placeId")
    })
    public abstract VehicleDTO toDto(Vehicle vehicle);

    @Mappings({
            @Mapping(source = "isRoadworthy", target = "roadworthy"),
            @Mapping(source = "isInsuranceValid", target = "insuranceValid"),
            @Mapping(source = "placeId", target = "place")
    })

    public abstract Vehicle toEntity(VehicleDTO vehicleDTO);

    // Méthode personnalisée pour convertir un ID en entité Place
    public Place longToPlace(Long placeId) {
        if (placeId == null) {
            return null;
        }
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new IllegalArgumentException("Place introuvable pour l’ID " + placeId));
    }
}
