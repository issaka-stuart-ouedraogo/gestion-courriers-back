package com.cil.bf.gestion_courriers.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cil.bf.gestion_courriers.models.Privilege;
import com.cil.bf.gestion_courriers.models.Profil;
import com.cil.bf.gestion_courriers.models.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByMatricule(String matricule);

    @Query("SELECT u.profilList FROM User u WHERE u.id = :id")
    List<Profil> findAllProfil(Long id);

    @Query("SELECT p.privilegeList FROM User u JOIN u.profilList p WHERE u.id = :id")
    List<Privilege> findAllPrivilege(Long id);

    Optional<User> findByProfilListId(Long id);
}
