package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.PersonDTO;
import com.eni.fleetviewer.back.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",uses = { IdToEntityMapper.class, PlaceMapper.class })
public interface PersonMapper {

    @Mappings({})
    PersonDTO toDto(Person person);

    @Mappings({})
    Person toEntity(PersonDTO dto);
}
