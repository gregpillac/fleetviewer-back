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
        this.address = person.getAddress() != null ? new AddressDTO(person.getAddress()) : null;
        this.place = person.getPlace() != null ? new PlaceDTO(person.getPlace()) : null;
    }

    public Person toEntity() {
        Person p = new Person();
        p.setId(this.id);
        p.setFirstName(this.firstName);
        p.setLastName(this.lastName);
        p.setEmail(this.email);
        p.setPhone(this.phone);
        p.setAddress(this.address != null ? this.address.toEntity() : null);
        p.setPlace(this.place != null ? this.place.toEntity() : null);
        return p;
    }
}
