package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.VehicleDTO;
import com.eni.fleetviewer.back.model.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",uses = { IdToEntityMapper.class })
public interface VehicleMapper {

    @Mapping(source = "place.id", target = "placeId")
    @Mapping(source = "isRoadworthy", target = "isRoadworthy")
    @Mapping(source = "isInsuranceValid", target = "isInsuranceValid")
    VehicleDTO toDto(Vehicle vehicle);

    @Mapping(source = "placeId", target = "place.id")
    @Mapping(source = "isRoadworthy", target = "isRoadworthy")
    @Mapping(source = "isInsuranceValid", target = "isInsuranceValid")
    Vehicle toEntity(VehicleDTO vehicleDTO);
}
