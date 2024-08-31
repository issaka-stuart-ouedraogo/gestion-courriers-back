package com.cil.bf.gestion_courriers.controllers.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cil.bf.gestion_courriers.dto.PrivilegeDto;
import com.cil.bf.gestion_courriers.dto.ProfilDto;

@RequestMapping(path = "/privileges")
public interface PrivilegeControllerInt {

    @PostMapping(path = "/save")
    public ResponseEntity<String> save(@RequestBody(required = true) PrivilegeDto privilegeDto);

    @PutMapping(path = "/update")
    public ResponseEntity<String> update(@RequestBody(required = true) PrivilegeDto privilegeDto);

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id);

    @GetMapping(path = "/all")
    public ResponseEntity<List<PrivilegeDto>> findAll();
}
