package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.PersonDTO;
import com.eni.fleetviewer.back.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class PersonMapper {

    @Mappings({})
    public abstract PersonDTO toDto(Person person);

    @Mappings({})
    public abstract Person toEntity(PersonDTO dto);
}
