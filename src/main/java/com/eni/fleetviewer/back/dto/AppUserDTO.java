package com.eni.fleetviewer.back.dto;

import com.eni.fleetviewer.back.model.Address;
import com.eni.fleetviewer.back.model.AppUser;
import com.eni.fleetviewer.back.model.Person;
import com.eni.fleetviewer.back.model.Place;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private RoleDTO role;
    private PersonDTO person;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public AppUserDTO(AppUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.enabled = user.isEnabled();
        this.role = user.getRole() != null ? new RoleDTO(user.getRole()) : null;
        this.person = user.getPerson() != null ? new PersonDTO(user.getPerson()) : null;
    }

    public AppUser toEntity() {
        AppUser u = new AppUser();
        u.setUsername(username);
        u.setEnabled(enabled);
        u.setRole(this.role != null ? this.role.toEntity() : null);
        u.setPerson(this.person != null ? this.person.toEntity() : null);
        return u;
    }
}
