package com.cil.bf.gestion_courriers_back.services.interfaces;

import org.springframework.http.ResponseEntity;
import java.util.List;

import com.cil.bf.gestion_courriers_back.Auth.dto.AuthDto;
import com.cil.bf.gestion_courriers_back.dto.PrivilegeDto;
import com.cil.bf.gestion_courriers_back.dto.ProfilDto;
import com.cil.bf.gestion_courriers_back.dto.ResquestIdDto;
import com.cil.bf.gestion_courriers_back.dto.UtilisateurDto;

public interface UtilsateurServiceInt {
    public ResponseEntity<String> save(UtilisateurDto utilisateurDto);

    public ResponseEntity<String> update(UtilisateurDto utilisateurDto);

    public ResponseEntity<UtilisateurDto> findByEmail(String email);

    public ResponseEntity<UtilisateurDto> findByLogin(String login);

    public ResponseEntity<UtilisateurDto> findByMatricule(String matricule);

    public ResponseEntity<List<UtilisateurDto>> findAll();

    public ResponseEntity<List<ProfilDto>> findAllProfil(ResquestIdDto resquestIdDto);

    public ResponseEntity<List<PrivilegeDto>> findAllPrivilege(ResquestIdDto resquestIdDto);

    public Boolean isUserGood(AuthDto authRequest);

    public Boolean isUserActif(AuthDto authRequest);
}
