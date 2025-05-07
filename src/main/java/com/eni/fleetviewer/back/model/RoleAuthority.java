package com.eni.fleetviewer.back.model;

import com.eni.fleetviewer.back.model.id.RoleAuthorityId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(RoleAuthorityId.class)
@Table(name = "roles_authorities")
public class RoleAuthority {

    // Clé primaire composite définie dans la classe RoleAuthorityId
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "authority_id")
    private Authority authority;
}
