package com.cil.bf.gestion_courriers.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cil.bf.gestion_courriers.models.Profil;

public interface ProfilRepo extends JpaRepository<Profil, Long> {
    Profil findByLibelle(String libelle);

    Profil findByCode(String code);

    Optional<Profil> findByPrivilegeListId(Long id);
}
