package com.eni.fleetviewer.back.Enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Roles {
    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    USER("USER"),
    DEFAULT("DEFAULT");

    private final String role;

    public String getValue() {
        return this.role;
    }

    public String getAuthority() {
        return "ROLE_" + this.role;
    }
}