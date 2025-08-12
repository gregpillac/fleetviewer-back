package com.eni.fleetviewer.back.dto;

import com.eni.fleetviewer.back.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private String id;
    private String description;

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.description = role.getDescription();
    }

    public Role toEntity() {
        Role r = new Role();
        r.setId(this.id);
        r.setDescription(this.description);
        return r;
    }
}
