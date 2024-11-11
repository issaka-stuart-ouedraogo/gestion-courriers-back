package com.cil.bf.gestion_courriers_back.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.cil.bf.gestion_courriers_back.dto.PrivilegeDto;
import com.cil.bf.gestion_courriers_back.models.Privileges;
import com.cil.bf.gestion_courriers_back.models.Profils;
import com.cil.bf.gestion_courriers_back.models.Utilisateurs;

import jakarta.persistence.QueryHint;
import jakarta.persistence.SqlResultSetMapping;

public interface UtilisateurRepo extends JpaRepository<Utilisateurs, Long> {
    Optional<Utilisateurs> findByEmail(String email);

    Optional<Utilisateurs> findByMatricule(String matricule);

    @Query("SELECT u.profilList FROM Utilisateurs u WHERE u.id = :id")
    List<Profils> findAllProfil(Long id);

    // @Query(value = "SELECT p.* FROM privileges p JOIN profils_privileges pp ON
    // p.id = pp.privilege_id JOIN utilisateurs_profils up ON pp.profil_id =
    // up.profil_id WHERE up.utilisateur_id = :id", nativeQuery = true)
    // List<Privileges> findAllPrivilege(@Param("id") Long id);

    Optional<Utilisateurs> findByProfilListId(Long id);

    Optional<Utilisateurs> findOneByMatricule(String matricule);
}
