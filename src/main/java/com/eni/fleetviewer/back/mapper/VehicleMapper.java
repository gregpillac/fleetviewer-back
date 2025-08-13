package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.VehicleDTO;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import com.eni.fleetviewer.back.model.Place;
import com.eni.fleetviewer.back.model.Vehicle;
import com.eni.fleetviewer.back.repository.PlaceRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class VehicleMapper {

    /**
     * Injection du repository Place pour la conversion d'ID en entité Place
     * MapStruct ne gère pas l’injection de dépendances via le constructeur pour les mappers abstraits.
     * Il génère une implémentation concrète à la compilation.
     * La classe doit donc être abstraite pour permettre cette génération.
     */
    @Autowired
    protected PlaceRepository placeRepository;

    @Mapping(source = "place.id", target = "placeId")
    @Mapping(source = "isRoadworthy", target = "isRoadworthy")
    @Mapping(source = "isInsuranceValid", target = "isInsuranceValid")
    public abstract VehicleDTO toDto(Vehicle vehicle);


    @Mapping(source = "placeId", target = "place", qualifiedByName = "longToPlace")
    @Mapping(source = "isRoadworthy", target = "isRoadworthy")
    @Mapping(source = "isInsuranceValid", target = "isInsuranceValid")
    public abstract Vehicle toEntity(VehicleDTO vehicleDTO);


    @Named("longToPlace")
    public Place longToPlace(Long placeId) {
        if (placeId == null) return null;
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new RessourceNotFoundException("Site introuvable pour l’ID " + placeId));
    }
}
