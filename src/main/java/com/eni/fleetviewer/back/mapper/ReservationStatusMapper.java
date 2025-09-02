package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.ReservationStatusDTO;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import com.eni.fleetviewer.back.model.ReservationStatus;
import com.eni.fleetviewer.back.repository.ReservationStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ReservationStatusMapper {

    @Autowired
    protected ReservationStatusRepository reservationStatusRepository;

    /**
     * Convertit l'entité Status en DTO.
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    public abstract ReservationStatusDTO toDto(ReservationStatus entity);

    /**
     * Convertit le DTO Status en entité.
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    public abstract ReservationStatus toEntity(ReservationStatusDTO dto);

    /**
     * Méthode utilitaire pour mapper un ID vers une entité Status.
     */
    @Named("reservationStatusIdToEntity")
    public ReservationStatus reservationStatusIdToEntity(Long id) {
        if (id == null) return null;
        return reservationStatusRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Statut de réservation introuvable pour l'ID " + id));
    }
}