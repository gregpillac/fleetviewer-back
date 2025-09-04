package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.AppUserDTO;
import com.eni.fleetviewer.back.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",uses = { IdToEntityMapper.class, PlaceMapper.class })
public interface UserMapper {

    @Mappings({})
    AppUserDTO toDto(AppUser person);

    @Mappings({})
    AppUser toEntity(AppUserDTO dto);

}
