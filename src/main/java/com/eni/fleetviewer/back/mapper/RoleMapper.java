package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.RoleDTO;
import com.eni.fleetviewer.back.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mappings({})
    RoleDTO toDto(Role role);

    @Mappings({})
    Role toEntity(RoleDTO dto);
}