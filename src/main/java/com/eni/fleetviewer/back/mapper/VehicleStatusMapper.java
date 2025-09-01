package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.VehicleStatusDTO;
import com.eni.fleetviewer.back.model.VehicleStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class VehicleStatusMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    public abstract VehicleStatusDTO toDto(VehicleStatus entity);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    public abstract VehicleStatus toEntity(VehicleStatusDTO dto);
}