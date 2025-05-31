package com.eni.fleetviewer.back.dto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class AuthResponse {
    private String username;
    private boolean authenticated;
    private String role;


    public AuthResponse(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public AuthResponse(boolean authenticated, String role) {
        this.role = role;
        this.authenticated = authenticated;
    }
}
