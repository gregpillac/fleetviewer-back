package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.PlaceDTO;
import com.eni.fleetviewer.back.exception.RessourceNotFoundException;
import com.eni.fleetviewer.back.model.Address;
import com.eni.fleetviewer.back.model.Place;
import com.eni.fleetviewer.back.model.PlaceType;
import com.eni.fleetviewer.back.model.Person;
import com.eni.fleetviewer.back.repository.AddressRepository;
import com.eni.fleetviewer.back.repository.PersonRepository;
import com.eni.fleetviewer.back.repository.PlaceTypeRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PlaceMapper {

    /**
     * Injection du repository Place pour la conversion d'ID en entité Place
     * MapStruct ne gère pas l’injection de dépendances via le constructeur pour les mappers abstraits.
     * Il génère une implémentation concrète à la compilation.
     * La classe doit donc être abstraite pour permettre cette génération et l'injection par champ obligatoire
     */
    @Autowired
    protected PlaceTypeRepository placeTypeRepository;

    @Autowired
    protected AddressRepository addressRepository;

    @Autowired
    protected PersonRepository personRepository;

    @Mappings({
            @Mapping(source = "placeType.id", target = "placeTypeId"),
            @Mapping(source = "address.id", target = "addressId"),
            @Mapping(source = "createdBy.id", target = "createdById")
    })
    public abstract PlaceDTO toDto(Place place);

    @Mappings({
            @Mapping(source = "placeTypeId", target = "placeType"),
            @Mapping(source = "addressId", target = "address"),
            @Mapping(source = "createdById", target = "createdBy")
    })
    public abstract Place toEntity(PlaceDTO dto);

    public PlaceType longToPlaceType(Long id) {
        if (id == null) return null;
        return placeTypeRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Type de lieu introuvable pour l’ID " + id));
    }

    public Address longToAddress(Long id) {
        if (id == null) return null;
        return addressRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Adresse introuvable pour l’ID " + id));
    }

    public Person longToCreatedBy(Long id) {
        if (id == null) return null;
        return personRepository.findById(id)
                .orElseThrow(() -> new RessourceNotFoundException("Individu introuvable pour l’ID " + id));
    }
}
