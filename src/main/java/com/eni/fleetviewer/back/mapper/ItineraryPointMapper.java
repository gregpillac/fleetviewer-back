package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.ItineraryPointDTO;
import com.eni.fleetviewer.back.model.ItineraryPoint;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {IdToEntityMapper.class})
public interface ItineraryPointMapper {

    @Mapping(source = "reservation.id", target = "reservationId")
    @Mapping(source = "place.id", target = "placeId")
    ItineraryPointDTO toDto(ItineraryPoint entity);

    @Mapping(source = "reservationId", target = "reservation")
    @Mapping(source = "placeId", target = "place")
    ItineraryPoint toEntity(ItineraryPointDTO dto);
}
