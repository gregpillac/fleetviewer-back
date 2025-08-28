package com.eni.fleetviewer.back.dto;

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
}