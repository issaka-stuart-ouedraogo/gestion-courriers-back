package com.cil.bf.gestion_courriers_back.validator;

import org.springframework.util.StringUtils;
import java.util.List;
import java.util.ArrayList;

import com.cil.bf.gestion_courriers_back.dto.UtilisateurDto;

public class UtilisateurValidator {
    public static List<String> valider(UtilisateurDto userDto) {
        List<String> error = new ArrayList<>();

        if (userDto == null) {
            error.add("L'utlisateur vide");
            return error;
        }
        if (!StringUtils.hasLength(userDto.getMatricule())) {
            error.add("Champ Matricule est vide!");
        }
        if (!StringUtils.hasLength(userDto.getContact())) {
            error.add("Champ Contact est vide!");
        }
        if (!StringUtils.hasLength(userDto.getNom())) {
            error.add("Champ Nom est vide!");
        }
        if (!StringUtils.hasLength(userDto.getPrenom())) {
            error.add("Champ Prenom est vide!");
        }
        if (!StringUtils.hasLength(userDto.getEmail())) {
            error.add("Champ Email est vide!");
        }
        if (userDto.getProfilDtoList() == null || userDto.getProfilDtoList().isEmpty()) {
            error.add("Champ Profil vide!");
        }
        if (userDto.getId() == null || userDto.getId() == 0) {
            if (!StringUtils.hasLength(userDto.getPassword())) {
                error.add("Champ Mot de passe est vide!");
            }
        }

        return error;
    }
}
