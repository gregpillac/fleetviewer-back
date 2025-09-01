package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.AddressDTO;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import com.eni.fleetviewer.back.model.Address;
import com.eni.fleetviewer.back.repository.AddressRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AddressMapper {

    @Autowired
    protected AddressRepository addressRepository;

    /**
     * Convertit l'entité Address en AddressDTO.
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "addressFirstLine", target = "addressFirstLine")
    @Mapping(source = "addressSecondLine", target = "addressSecondLine")
    @Mapping(source = "postalCode", target = "postalCode")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "gpsCoords", target = "gpsCoords")
    public abstract AddressDTO addressToAddressDTO(Address address);

    /**
     * Convertit AddressDTO en entité Address.
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "addressFirstLine", target = "addressFirstLine")
    @Mapping(source = "addressSecondLine", target = "addressSecondLine")
    @Mapping(source = "postalCode", target = "postalCode")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "gpsCoords", target = "gpsCoords")
    public abstract Address addressDTOToAddress(AddressDTO addressDTO);


    @Named("addressIdToAddress")
    public Address addressIdToAddress(Long id) {
        if (id == null) return null;
        return addressRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Adresse introuvable pour l'ID " + id));
    }
}