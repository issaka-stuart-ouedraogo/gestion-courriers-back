package com.cil.bf.gestion_courriers.validator;

import org.springframework.util.StringUtils;
import java.util.List;
import java.util.ArrayList;

import com.cil.bf.gestion_courriers.dto.UserDto;

public class UserValidator {
    public static List<String> valider(UserDto userDto) {
        List<String> error = new ArrayList<>();

        if (userDto == null) {
            error.add("L'utlisateur vide");
            return error;
        }
        if (!StringUtils.hasLength(userDto.getMatricule())) {
            error.add("Matricule vide!");
        }
        if (!StringUtils.hasLength(userDto.getEmail())) {
            error.add("Email vide!");
        }
        return error;
    }
}
