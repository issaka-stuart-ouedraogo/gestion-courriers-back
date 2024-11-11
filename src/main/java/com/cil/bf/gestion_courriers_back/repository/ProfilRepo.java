package com.cil.bf.gestion_courriers_back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cil.bf.gestion_courriers_back.models.Profils;

public interface ProfilRepo extends JpaRepository<Profils, Long> {
    Profils findByLibelle(String libelle);

    Profils findByCode(String code);

    Optional<Profils> findByPrivilegeListId(Long id);
}
