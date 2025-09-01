package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.ReservationStatusDTO;
import com.eni.fleetviewer.back.model.ReservationStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationStatusMapper {
    ReservationStatusDTO toDto(ReservationStatus entity);
    ReservationStatus toEntity(ReservationStatusDTO dto);
}
