package com.cil.bf.gestion_courriers.validator;

import org.springframework.util.StringUtils;
import java.util.List;
import java.util.ArrayList;

import com.cil.bf.gestion_courriers.dto.ProfilDto;

public class ProfilValidator {
    public static List<String> valider(ProfilDto profilDto) {
        List<String> error = new ArrayList<>();

        if (profilDto == null) {
            error.add("Le profil est vide");
            return error;
        }
        if (!StringUtils.hasLength(profilDto.getLibelle())) {
            error.add("Libellé vide!");
        }
        if (!StringUtils.hasLength(profilDto.getCode())) {
            error.add("Code vide!");
        }
        return error;
    }
}
