package com.eni.fleetviewer.back.dto;

import com.eni.fleetviewer.back.model.Address;
import com.eni.fleetviewer.back.model.AppUser;
import com.eni.fleetviewer.back.model.Person;
import com.eni.fleetviewer.back.model.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {
    private Long id;
    private String username;
    private boolean enabled;
    private String role;
    private PersonDTO person;

    public AppUserDTO(AppUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.enabled = user.isEnabled();
        this.role = user.getRoleName();
        if (user.getPerson() != null) {
            this.person = new PersonDTO(user.getPerson());
        }
    }
}
