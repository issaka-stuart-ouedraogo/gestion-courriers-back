package com.cil.bf.gestion_courriers.controllers.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cil.bf.gestion_courriers.controllers.interfaces.PrivilegeControllerInt;
import com.cil.bf.gestion_courriers.dto.PrivilegeDto;
import com.cil.bf.gestion_courriers.dto.ProfilDto;
import com.cil.bf.gestion_courriers.repository.PrivilegeRepo;
import com.cil.bf.gestion_courriers.utils.CourriersUtils;
import com.cil.bf.gestion_courriers.utils.Constants;

import com.cil.bf.gestion_courriers.services.interfaces.PrivilegeServiceInt;

@RestController
public class PrivilegeControllerImpl implements PrivilegeControllerInt {

    @Autowired
    PrivilegeServiceInt privilegeService;

    @Override
    public ResponseEntity<String> save(PrivilegeDto privilegeDto) {
        try {
            return privilegeService.save(privilegeDto);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(PrivilegeDto privilegeDto) {
        try {
            return privilegeService.update(privilegeDto);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> delete(Long id) {
        try {
            return privilegeService.delete(id);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<PrivilegeDto>> findAll() {
        try {
            return privilegeService.findAll();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<List<PrivilegeDto>>(new ArrayList<>(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
