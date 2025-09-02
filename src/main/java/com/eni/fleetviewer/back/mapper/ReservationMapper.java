package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.ReservationDTO;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import com.eni.fleetviewer.back.model.Person;
import com.eni.fleetviewer.back.model.Reservation;
import com.eni.fleetviewer.back.model.ReservationStatus;
import com.eni.fleetviewer.back.model.Vehicle;
import com.eni.fleetviewer.back.repository.PersonRepository;
import com.eni.fleetviewer.back.repository.ReservationStatusRepository;
import com.eni.fleetviewer.back.repository.VehicleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ReservationMapper {

    @Autowired
    protected VehicleRepository vehicleRepository;
    @Autowired
    protected PersonRepository personRepository;
    @Autowired
    protected ReservationStatusRepository reservationStatusRepository;

    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "reservationStatus.id", target = "reservationStatusId")
    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "driver.id", target = "driverId")
    public abstract ReservationDTO toDto(Reservation reservation);

    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "reservationStatusId", target = "reservationStatus", qualifiedByName = "longToReservationStatus")
    @Mapping(source = "vehicleId", target = "vehicle", qualifiedByName = "longToVehicle")
    @Mapping(source = "driverId", target = "driver", qualifiedByName = "longToDriver")
    public abstract Reservation toEntity(ReservationDTO reservationDTO);

    @Named("longToVehicle")
    public Vehicle longToVehicle(Long vehicleId) {
        if (vehicleId == null) return null;
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RessourceNotFoundException("Véhicule introuvable pour l'ID " + vehicleId));
    }

    @Named("longToDriver")
    public Person longToDriver(Long driverId) {
        if (driverId == null) return null;
        return personRepository.findById(driverId)
                .orElseThrow(() -> new RessourceNotFoundException("Personne introuvable pour l'ID " + driverId));
    }

    @Named("longToReservationStatus")
    public ReservationStatus longToReservationStatus(Long statusId) {
        if (statusId == null) return null;
        // Assurez-vous que ReservationStatusRepository existe
        return reservationStatusRepository.findById(statusId)
                .orElseThrow(() -> new RessourceNotFoundException("Statut de réservation introuvable pour l'ID " + statusId));
    }
}
