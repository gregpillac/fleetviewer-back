package com.eni.fleetviewer.back.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {
    @NotBlank(message = "L'ancien mot de passe est requis")
    private String currentPassword;
    @NotBlank(message = "Le nouveau mot de passe est requis")
    private String newPassword;
}
