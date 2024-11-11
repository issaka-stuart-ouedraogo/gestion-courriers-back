package com.cil.bf.gestion_courriers_back.validator;

import org.springframework.util.StringUtils;

import com.cil.bf.gestion_courriers_back.Auth.dto.ResetPasswordDto;

import java.util.List;
import java.util.ArrayList;

public class RestPasswordValidator {
    public static List<String> valider(ResetPasswordDto resetPasswordDto) {
        List<String> error = new ArrayList<>();

        if (resetPasswordDto == null) {
            error.add("ResetPasswordDto vide");
            return error;
        }
        if (!StringUtils.hasLength(resetPasswordDto.getNewPassword())) {
            error.add("New Mot de passe vide!");
        }
        if (!StringUtils.hasLength(resetPasswordDto.getOldPassword())) {
            error.add("Old matricule de passe vide!");
        }
        return error;
    }
}
