package com.cil.bf.gestion_courriers.services.interfaces;

import org.springframework.http.ResponseEntity;
import java.util.List;

import com.cil.bf.gestion_courriers.dto.AuthDto;
import com.cil.bf.gestion_courriers.dto.PrivilegeDto;
import com.cil.bf.gestion_courriers.dto.ProfilDto;
import com.cil.bf.gestion_courriers.dto.UserDto;

public interface UserServiceInt {
    public ResponseEntity<String> save(UserDto userDto);

    public ResponseEntity<String> update(UserDto userDto);

    public ResponseEntity<String> login(AuthDto authDto);

    public ResponseEntity<List<UserDto>> findAll();

    public ResponseEntity<List<ProfilDto>> findAllProfil(Long id);

    public ResponseEntity<List<PrivilegeDto>> findAllPrivilege(Long id);
}
