package com.cil.bf.gestion_courriers_back.dto;

import com.cil.bf.gestion_courriers_back.models.Privileges;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrivilegeDto {

    private Long id;

    private String code;

    private String libelle;

    public static PrivilegeDto fromEntity(Privileges privilege) {

        PrivilegeDto privilegeDto = PrivilegeDto.builder()
                .id(privilege.getId())
                .code(privilege.getCode().toUpperCase())
                .libelle(privilege.getLibelle().toUpperCase())
                .build();
        return privilegeDto;
    }

    public static Privileges toEntity(PrivilegeDto privilegeDto) {

        Privileges privilege = new Privileges();

        privilege.setId(privilegeDto.getId());
        privilege.setCode(privilegeDto.getCode().toUpperCase());
        privilege.setLibelle(privilegeDto.getLibelle().toUpperCase());
        return privilege;
    }
}
