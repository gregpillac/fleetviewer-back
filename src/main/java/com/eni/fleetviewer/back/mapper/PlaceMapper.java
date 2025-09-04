package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.PlaceDTO;
import com.eni.fleetviewer.back.model.Place;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {IdToEntityMapper.class})
public interface PlaceMapper {

    @Mapping(source = "placeType.id", target = "placeTypeId")
    @Mapping(source = "address.id", target = "addressId")
    PlaceDTO toDto(Place place);

    @Mapping(source = "placeTypeId", target = "placeType")
    @Mapping(target = "address", source = "addressId")
    Place toEntity(PlaceDTO dto);
}
