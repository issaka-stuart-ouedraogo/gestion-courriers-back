package com.cil.bf.gestion_courriers.services.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;
import com.cil.bf.gestion_courriers.dto.PrivilegeDto;

public interface PrivilegeServiceInt {

    public ResponseEntity<String> save(PrivilegeDto privilegeDto);

    public ResponseEntity<String> update(PrivilegeDto privilegeDto);

    public ResponseEntity<String> delete(Long id);

    public ResponseEntity<List<PrivilegeDto>> findAll();
}
