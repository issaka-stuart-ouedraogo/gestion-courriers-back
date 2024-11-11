package com.cil.bf.gestion_courriers_back.Auth.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Objet View Model pour stocker les informations d’identification d’un
 * utilisateur (à la connexion) .
 */
@Data
public class AuthDto {
    @NotNull
    @Size(min = 1, max = 10)
    private String matricule;

    @NotNull
    @Size(min = 6, max = 100)
    private String password;

    private Boolean rememberMe;
}
