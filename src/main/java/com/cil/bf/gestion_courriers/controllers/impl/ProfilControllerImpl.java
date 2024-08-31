package com.cil.bf.gestion_courriers.controllers.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cil.bf.gestion_courriers.controllers.interfaces.ProfilControllerInt;
import com.cil.bf.gestion_courriers.dto.ProfilDto;
import com.cil.bf.gestion_courriers.services.interfaces.ProfilServiceInt;
import com.cil.bf.gestion_courriers.utils.Constants;
import com.cil.bf.gestion_courriers.utils.CourriersUtils;

@RestController
public class ProfilControllerImpl implements ProfilControllerInt {

    @Autowired
    private ProfilServiceInt profilService;

    @Override
    public ResponseEntity<String> save(ProfilDto profilDto) {
        try {
            return profilService.save(profilDto);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(ProfilDto profilDto) {
        try {
            return profilService.update(profilDto);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        try {
            return profilService.delete(id);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProfilDto>> findAll() {
        try {
            return profilService.findAll();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<List<ProfilDto>>(new ArrayList<>(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
