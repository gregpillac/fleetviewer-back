package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.ReservationDTO;
import com.eni.fleetviewer.back.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",uses = { IdToEntityMapper.class, ItineraryPointMapper.class })
public interface ReservationMapper {

    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "reservationStatus.id", target = "reservationStatusId")
    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(target = "itineraryPoints", ignore = true)  // Ignoré, car il n'y a pas de champ correspondant dans l'entité
    ReservationDTO toDto(Reservation reservation);

    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    // MapStruct va chercher dans les mappers qu'il utilise une méthode qui prend un type source en paramètre et qui retourne un type du champ cible.
    @Mapping(source = "reservationStatusId", target = "reservationStatus")
    @Mapping(source = "vehicleId", target = "vehicle")
    @Mapping(source = "driverId", target = "driver")
    @Mapping(target = "itineraryPoints", ignore = true)  // ESSENTIEL : Ignorer ce champ car il n'existe pas dans l'entité Reservation
    Reservation toEntity(ReservationDTO reservationDTO);
}
