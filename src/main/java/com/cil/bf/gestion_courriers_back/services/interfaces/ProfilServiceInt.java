package com.cil.bf.gestion_courriers_back.services.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cil.bf.gestion_courriers_back.dto.ProfilDto;

public interface ProfilServiceInt {
    public ResponseEntity<String> save(ProfilDto profilDto);

    public ResponseEntity<String> update(ProfilDto profilDto);

    public ResponseEntity<String> delete(Long id);

    public ResponseEntity<List<ProfilDto>> findAll();

}
