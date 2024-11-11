package com.cil.bf.gestion_courriers_back.validator;

import org.springframework.util.StringUtils;
import java.util.List;
import java.util.ArrayList;

import com.cil.bf.gestion_courriers_back.dto.PrivilegeDto;

public class PrivilegeValidator {
    public static List<String> valider(PrivilegeDto privilegeDto) {
        List<String> error = new ArrayList<>();

        if (privilegeDto == null) {
            error.add("Privilege vide");
            return error;
        }
        if (!StringUtils.hasLength(privilegeDto.getLibelle())) {
            error.add("Libbellé vide!");
        }
        if (!StringUtils.hasLength(privilegeDto.getCode())) {
            error.add("Code vide!");
        }
        return error;
    }
}
