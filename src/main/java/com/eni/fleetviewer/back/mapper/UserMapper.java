package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.AppUserDTO;
import com.eni.fleetviewer.back.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Mappings({})
    public abstract AppUserDTO toDto(AppUser person);

    @Mappings({})
    public abstract AppUser toEntity(AppUserDTO dto);

}
