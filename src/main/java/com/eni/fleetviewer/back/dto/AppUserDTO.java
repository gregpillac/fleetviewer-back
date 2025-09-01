package com.eni.fleetviewer.back.dto;

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
}
