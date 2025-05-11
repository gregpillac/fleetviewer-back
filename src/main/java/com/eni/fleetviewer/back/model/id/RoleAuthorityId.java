package com.eni.fleetviewer.back.model.id;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleAuthorityId implements Serializable {

    private String role;
    private String authority;
}
