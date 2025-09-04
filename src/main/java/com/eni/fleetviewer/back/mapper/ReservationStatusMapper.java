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
public interface ReservationStatusMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    ReservationStatusDTO toDto(ReservationStatus entity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    ReservationStatus toEntity(ReservationStatusDTO dto);
}