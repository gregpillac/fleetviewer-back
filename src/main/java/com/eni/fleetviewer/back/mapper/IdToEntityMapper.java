package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import com.eni.fleetviewer.back.model.*;
import com.eni.fleetviewer.back.repository.*;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Ce composant centralise la logique de conversion d'un ID (Long)
 * vers une entité JPA complète. Il est utilisé par d'autres mappers.
 */
@Mapper(componentModel = "spring")
public abstract class IdToEntityMapper {

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private VehicleKeyRepository vehicleKeyRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PlaceTypeRepository placeTypeRepository;

    public VehicleKey longToVehicleKey(Long keyId) {
        if (keyId == null) return null;
        return vehicleKeyRepository.findById(keyId)
                .orElseThrow(() -> new RessourceNotFoundException("Clé de véhicule introuvable pour l'ID " + keyId));
    }

    public Vehicle longToVehicle(Long vehicleId) {
        if (vehicleId == null) return null;
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RessourceNotFoundException("Véhicule introuvable pour l'ID " + vehicleId));
    }

    public Person longToDriver(Long driverId) {
        if (driverId == null) return null;
        return personRepository.findById(driverId)
                .orElseThrow(() -> new RessourceNotFoundException("Personne introuvable pour l'ID " + driverId));
    }

    public Reservation longToReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Réservation introuvable pour l'ID " + id));
    }

    public Place longToPlace(Long placeId) {
        if (placeId == null) return null;
        return placeRepository.findById(placeId)
                .orElseThrow(() -> new RessourceNotFoundException("Site introuvable pour l’ID " + placeId));
    }

    public Address longToAddress(Long addressId) {
        if (addressId == null) return null;
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new RessourceNotFoundException("Adresse introuvable pour l’ID " + addressId));
    }

    public PlaceType longToPlaceType(Long id) {
        if (id == null) return null;
        return placeTypeRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Type de lieu introuvable pour l’ID " + id));
    }
}
