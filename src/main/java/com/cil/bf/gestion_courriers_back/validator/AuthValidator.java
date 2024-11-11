package com.cil.bf.gestion_courriers_back.validator;

import org.springframework.util.StringUtils;

import com.cil.bf.gestion_courriers_back.Auth.dto.AuthDto;

import java.util.List;
import java.util.ArrayList;

public class AuthValidator {
    public static List<String> valider(AuthDto authDto) {
        List<String> error = new ArrayList<>();

        if (authDto == null) {
            error.add("Auth vide");
            return error;
        }
        if (!StringUtils.hasLength(authDto.getMatricule())) {
            error.add("Matricule vide!");
        }
        if (!StringUtils.hasLength(authDto.getPassword())) {
            error.add("Mot de passe vide!");
        }
        return error;
    }
}
