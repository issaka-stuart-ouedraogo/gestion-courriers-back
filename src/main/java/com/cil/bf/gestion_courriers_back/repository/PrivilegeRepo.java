package com.cil.bf.gestion_courriers_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cil.bf.gestion_courriers_back.models.Privileges;

public interface PrivilegeRepo extends JpaRepository<Privileges, Long> {

    Privileges findByLibelle(String libelle);

    Privileges findByCode(String code);
}
