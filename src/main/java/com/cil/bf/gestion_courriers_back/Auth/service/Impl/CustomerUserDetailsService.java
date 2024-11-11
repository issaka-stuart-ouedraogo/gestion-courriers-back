package com.cil.bf.gestion_courriers_back.Auth.service.Impl;

import com.cil.bf.gestion_courriers_back.repository.UtilisateurRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.cil.bf.gestion_courriers_back.dto.PrivilegeDto;
import com.cil.bf.gestion_courriers_back.dto.UtilisateurDto;
import com.cil.bf.gestion_courriers_back.models.Utilisateurs;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

  @Autowired
  private UtilisateurRepo utilisateurRepo;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    return utilisateurRepo.findByMatricule(username)
        .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé."));
  }
}
