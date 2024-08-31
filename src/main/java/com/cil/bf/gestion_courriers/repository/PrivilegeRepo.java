package com.cil.bf.gestion_courriers.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cil.bf.gestion_courriers.models.Privilege;

public interface PrivilegeRepo extends JpaRepository<Privilege, Long> {

    Privilege findByLibelle(String libelle);

    Privilege findByCode(String code);
}
