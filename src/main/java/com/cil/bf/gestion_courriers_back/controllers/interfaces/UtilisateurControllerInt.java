package com.cil.bf.gestion_courriers_back.controllers.interfaces;

import com.cil.bf.gestion_courriers_back.dto.PrivilegeDto;
import com.cil.bf.gestion_courriers_back.dto.ProfilDto;
import com.cil.bf.gestion_courriers_back.dto.ResquestIdDto;
import com.cil.bf.gestion_courriers_back.dto.UtilisateurDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(path = "/users")
public interface UtilisateurControllerInt {

    @PostMapping(path = "/signup")
    public ResponseEntity<String> save(@RequestBody(required = true) UtilisateurDto utilisateuDto);

    @PutMapping(path = "/update")
    public ResponseEntity<String> update(@RequestBody(required = true) UtilisateurDto utilisateuDto);

    @PostMapping(path = "/delete/profil")
    public ResponseEntity<String> deleteProfil(@RequestBody(required = true) ResquestIdDto resquestIdDto);

    @GetMapping(path = "/all")
    public ResponseEntity<List<UtilisateurDto>> findAll();

    @PostMapping(path = "/profils/all")
    public ResponseEntity<List<ProfilDto>> findAllProfil(@RequestBody(required = true) ResquestIdDto resquestIdDto);

    @PostMapping(path = "/privileges/all")
    public ResponseEntity<List<PrivilegeDto>> findAllPrivilege(
            @RequestBody(required = true) ResquestIdDto resquestIdDto);

}
