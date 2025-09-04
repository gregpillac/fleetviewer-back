package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.AddressDTO;
import com.eni.fleetviewer.back.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring",uses = { IdToEntityMapper.class})
public interface AddressMapper {

    /**
     * Convertit une entité Address en AddressDTO.
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "addressFirstLine", target = "addressFirstLine")
    @Mapping(source = "addressSecondLine", target = "addressSecondLine")
    @Mapping(source = "postalCode", target = "postalCode")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "gpsCoords", target = "gpsCoords")
    AddressDTO toDto(Address address);

    /**
     * Convertit un AddressDTO en entité Address.
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "addressFirstLine", target = "addressFirstLine")
    @Mapping(source = "addressSecondLine", target = "addressSecondLine")
    @Mapping(source = "postalCode", target = "postalCode")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "gpsCoords", target = "gpsCoords")
    Address toEntity(AddressDTO addressDTO);
}