package com.cil.bf.gestion_courriers.controllers.impl;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.cil.bf.gestion_courriers.controllers.interfaces.UserControllerInt;
import com.cil.bf.gestion_courriers.dto.AuthDto;
import com.cil.bf.gestion_courriers.dto.PrivilegeDto;
import com.cil.bf.gestion_courriers.dto.ProfilDto;
import com.cil.bf.gestion_courriers.dto.UserDto;
import com.cil.bf.gestion_courriers.services.interfaces.UserServiceInt;
import com.cil.bf.gestion_courriers.utils.Constants;
import com.cil.bf.gestion_courriers.utils.CourriersUtils;

@RestController
public class UserControllerImpl implements UserControllerInt {

    @Autowired
    private UserServiceInt userService;

    @Override
    public ResponseEntity<String> save(UserDto userDto) {
        try {
            return userService.save(userDto);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(UserDto userDto) {
        try {
            return userService.update(userDto);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(AuthDto authDto) {
        try {
            return userService.login(authDto);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserDto>> findAll() {

        try {
            return userService.findAll();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<List<UserDto>>(new ArrayList<>(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProfil(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteProfil'");
    }

    @Override
    public ResponseEntity<List<ProfilDto>> findAllProfil(Long id) {
        try {
            return userService.findAllProfil(id);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<List<ProfilDto>>(new ArrayList<>(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<PrivilegeDto>> findAllPrivilege(Long id) {
        try {
            return userService.findAllPrivilege(id);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<List<PrivilegeDto>>(new ArrayList<>(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
