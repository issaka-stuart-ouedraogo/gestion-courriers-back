package com.cil.bf.gestion_courriers_back.controllers.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.cil.bf.gestion_courriers_back.controllers.interfaces.UtilisateurControllerInt;
import com.cil.bf.gestion_courriers_back.dto.PrivilegeDto;
import com.cil.bf.gestion_courriers_back.dto.ProfilDto;
import com.cil.bf.gestion_courriers_back.dto.ResquestIdDto;
import com.cil.bf.gestion_courriers_back.dto.UtilisateurDto;
import com.cil.bf.gestion_courriers_back.services.interfaces.UtilsateurServiceInt;

@RestController
public class UtilisateurControllerImpl implements UtilisateurControllerInt {

    @Autowired
    private UtilsateurServiceInt utilisateurService;

    @Override
    public ResponseEntity<String> save(UtilisateurDto userDto) {
        return utilisateurService.save(userDto);
    }

    @Override
    public ResponseEntity<String> update(UtilisateurDto userDto) {
        return utilisateurService.update(userDto);
    }

    @Override
    public ResponseEntity<List<UtilisateurDto>> findAll() {
        return utilisateurService.findAll();
    }

    @Override
    public ResponseEntity<String> deleteProfil(ResquestIdDto resquestIdDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteProfil'");
    }

    @Override
    public ResponseEntity<List<ProfilDto>> findAllProfil(ResquestIdDto resquestIdDto) {
        return utilisateurService.findAllProfil(resquestIdDto);
    }

    @Override
    public ResponseEntity<List<PrivilegeDto>> findAllPrivilege(ResquestIdDto resquestIdDto) {
        return utilisateurService.findAllPrivilege(resquestIdDto);
    }

}
