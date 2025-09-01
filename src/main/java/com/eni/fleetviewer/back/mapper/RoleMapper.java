package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.RoleDTO;
import com.eni.fleetviewer.back.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class RoleMapper {

    /**
     * Convertit une entité Role en RoleDTO.
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "description", target = "description")
    public abstract RoleDTO toDto(Role role);

    /**
     * Convertit un DTO Role en entité Role.
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "description", target = "description")
    public abstract Role toEntity(RoleDTO dto);
}