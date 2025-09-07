package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.VehicleKeyDTO;
import com.eni.fleetviewer.back.model.VehicleKey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { IdToEntityMapper.class })
public interface VehicleKeyMapper {

    // Entity -> DTO
    @Mapping(source = "vehicle.id", target = "vehicleId")
    @Mapping(source = "place.id", target = "placeId")
    VehicleKeyDTO toDto(VehicleKey vehicleKey);

    // DTO -> Entity
    @Mapping(source = "vehicleId", target = "vehicle.id")
    @Mapping(source = "placeId", target = "place.id")
    VehicleKey toEntity(VehicleKeyDTO dto);
}
