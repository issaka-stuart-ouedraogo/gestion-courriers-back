package com.cil.bf.gestion_courriers.controllers.interfaces;

import com.cil.bf.gestion_courriers.dto.AuthDto;
import com.cil.bf.gestion_courriers.dto.PrivilegeDto;
import com.cil.bf.gestion_courriers.dto.ProfilDto;
import com.cil.bf.gestion_courriers.dto.UserDto;

import org.hibernate.mapping.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(path = "/users")
public interface UserControllerInt {

    @PostMapping(path = "/signup")
    public ResponseEntity<String> save(@RequestBody(required = true) UserDto userDto);

    @PutMapping(path = "/update")
    public ResponseEntity<String> update(@RequestBody(required = true) UserDto userDto);

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody(required = true) AuthDto authDto);

    @PostMapping(path = "/delete/profil")
    public ResponseEntity<String> deleteProfil(@PathVariable("user_id") Long id);

    @GetMapping(path = "/all")
    public ResponseEntity<List<UserDto>> findAll();

    @PostMapping(path = "/{id}/profils/all")
    public ResponseEntity<List<ProfilDto>> findAllProfil(@PathVariable(name = "id", required = true) Long id);

    @PostMapping(path = "/{id}/privileges/all")
    public ResponseEntity<List<PrivilegeDto>> findAllPrivilege(@PathVariable(name = "id", required = true) Long id);

}
