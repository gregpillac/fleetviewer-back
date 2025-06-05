package com.eni.fleetviewer.back.dto;

import com.eni.fleetviewer.back.model.Person;
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

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
        this.phone = person.getPhone();
        if (person.getAddress() != null) {
            this.address = new AddressDTO(person.getAddress());
        }
        if (person.getPlace() != null) {
            this.place = new PlaceDTO(person.getPlace());
        }
    }
}
