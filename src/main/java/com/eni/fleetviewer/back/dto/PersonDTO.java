package com.eni.fleetviewer.back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private AddressDTO address;
    private PlaceDTO place;
}
