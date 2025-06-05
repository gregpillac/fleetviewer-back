package com.eni.fleetviewer.back.dto;

import com.eni.fleetviewer.back.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private Long id;
    private String addressFirstLine;
    private String addressSecondLine;
    private String postalCode;
    private String city;
    private String gpsCoords;

    public AddressDTO(Address address) {
        this.id = address.getId();
        this.addressFirstLine = address.getAddressFirstLine();
        this.addressSecondLine = address.getAddressSecondLine();
        this.postalCode = address.getPostalCode();
        this.city = address.getCity();
        this.gpsCoords = address.getGpsCoords();
    }
}
