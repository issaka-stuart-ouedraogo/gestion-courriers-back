package com.cil.bf.gestion_courriers.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.cil.bf.gestion_courriers.models.Profil;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfilDto {

    private Long id;

    private String code;

    private String libelle;

    private Set<PrivilegeDto> privilegeDtoList;

    public static ProfilDto fromEntity(Profil profil) {

        ProfilDto profildDto = ProfilDto.builder()
                .id(profil.getId())
                .code(profil.getCode())
                .libelle(profil.getLibelle())
                .privilegeDtoList(profil.getPrivilegeList() != null
                        ? profil.getPrivilegeList().stream().map(PrivilegeDto::fromEntity).collect(Collectors.toSet())
                        : null)
                .build();
        return profildDto;
    }

    public static Profil toEntity(ProfilDto profilDto) {

        Profil profil = new Profil();

        profil.setId(profilDto.getId());
        profil.setCode(profilDto.getCode().toUpperCase());
        profil.setLibelle(profilDto.getLibelle().toUpperCase());
        return profil;
    }
}
