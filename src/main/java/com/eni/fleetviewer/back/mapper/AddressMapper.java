package com.eni.fleetviewer.back.mapper;

import com.eni.fleetviewer.back.dto.AddressDTO;
import com.eni.fleetviewer.back.dto.AppUserDTO;
import com.eni.fleetviewer.back.model.Address;
import com.eni.fleetviewer.back.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class AddressMapper {

    @Mappings({})
    public abstract AddressDTO toDto(Address address);

    @Mappings({})
    public abstract Address toEntity(AddressDTO dto);;

}
