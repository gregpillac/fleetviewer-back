package com.eni.fleetviewer.back.dto;

import com.eni.fleetviewer.back.model.AppUser;
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
    private String fullName; // exemple avec Person

    public AppUserDTO(AppUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.enabled = user.isEnabled();
        this.role = user.getRoleName();
        this.fullName = user.getPerson().getFirstName() + " " + user.getPerson().getLastName();
    }
}
