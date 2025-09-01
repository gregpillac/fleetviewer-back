package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.ItineraryPointDTO;
import com.eni.fleetviewer.back.model.ItineraryPoint;
import com.eni.fleetviewer.back.model.Place;
import com.eni.fleetviewer.back.model.Reservation;
import com.eni.fleetviewer.back.repository.PlaceRepository;
import com.eni.fleetviewer.back.repository.ReservationRepository;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ItineraryPointMapper {

    @Autowired
    protected ReservationRepository reservationRepository;

    @Autowired
    protected PlaceRepository placeRepository;

    @Mapping(source = "reservation.id", target = "reservationId")
    @Mapping(source = "place.id", target = "placeId")
    public abstract ItineraryPointDTO toDto(ItineraryPoint entity);

    @Mapping(source = "reservationId", target = "reservation", qualifiedByName = "idToReservation")
    @Mapping(source = "placeId", target = "place", qualifiedByName = "idToPlace")
    public abstract ItineraryPoint toEntity(ItineraryPointDTO dto);

    @Named("idToReservation")
    public Reservation mapReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Réservation introuvable pour l'ID " + id));
    }

    @Named("idToPlace")
    public Place mapPlace(Long id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Lieu introuvable pour l'ID " + id));
    }

}
