package com.cil.bf.gestion_courriers_back.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.cil.bf.gestion_courriers_back.models.Profils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfilDto {

    private Long id;

    private String code;

    private String libelle;

    private Set<PrivilegeDto> privilegeDtoList;

    public static ProfilDto fromEntity(Profils profil) {

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

    public static Profils toEntity(ProfilDto profilDto) {

        Profils profil = new Profils();

        profil.setId(profilDto.getId());
        profil.setCode(profilDto.getCode().toUpperCase());
        profil.setLibelle(profilDto.getLibelle().toUpperCase());
        return profil;
    }
}
