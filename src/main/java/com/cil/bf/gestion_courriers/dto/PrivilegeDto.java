package com.cil.bf.gestion_courriers.dto;

import com.cil.bf.gestion_courriers.models.Privilege;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrivilegeDto {

    private Long id;

    private String code;

    private String libelle;

    private boolean deleted = false;

    public void setLibelle(String libelle) {
        this.libelle = libelle.toUpperCase();
    }

    public static PrivilegeDto fromEntity(Privilege privilege) {

        PrivilegeDto privilegeDto = PrivilegeDto.builder()
                .id(privilege.getId())
                .code(privilege.getCode())
                .libelle(privilege.getLibelle())
                .build();
        return privilegeDto;
    }

    public static Privilege toEntity(PrivilegeDto privilegeDto) {

        Privilege privilege = new Privilege();

        privilege.setId(privilegeDto.getId());
        privilege.setCode(privilegeDto.getCode().toUpperCase());
        privilege.setLibelle(privilegeDto.getLibelle().toUpperCase());
        return privilege;
    }
}
