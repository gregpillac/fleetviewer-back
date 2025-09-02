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

    /**
     * Injection du repository Address pour la conversion d’ID en entité Address.
     * MapStruct ne gère pas l’injection de dépendance via le constructeur dans les mappers abstraits.
     */
    @Autowired
    protected AddressRepository addressRepository;

    /**
     * Convertit une entité Address en AddressDTO.
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "addressFirstLine", target = "addressFirstLine")
    @Mapping(source = "addressSecondLine", target = "addressSecondLine")
    @Mapping(source = "postalCode", target = "postalCode")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "gpsCoords", target = "gpsCoords")
    public abstract AddressDTO toDto(Address address);

    /**
     * Convertit un AddressDTO en entité Address.
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "addressFirstLine", target = "addressFirstLine")
    @Mapping(source = "addressSecondLine", target = "addressSecondLine")
    @Mapping(source = "postalCode", target = "postalCode")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "gpsCoords", target = "gpsCoords")
    public abstract Address toEntity(AddressDTO addressDTO);

    /**
     * Méthode utilitaire pour convertir un ID en entité Address.
     * Utilisable dans d'autres mappings si nécessaire.
     */
    @Named("longToAddress")
    public Address longToAddress(Long id) {
        if (id == null) return null;
        return addressRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Adresse introuvable pour l’ID " + id));
    }

    public abstract AddressDTO addressToAddressDTO(Address address);

    public abstract Address addressDTOToAddress(AddressDTO addressDTO);
}