package com.cil.bf.gestion_courriers.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cil.bf.gestion_courriers.dto.AuthDto;
import com.cil.bf.gestion_courriers.dto.PrivilegeDto;
import com.cil.bf.gestion_courriers.dto.ProfilDto;
import com.cil.bf.gestion_courriers.dto.UserDto;
import com.cil.bf.gestion_courriers.models.Profil;
import com.cil.bf.gestion_courriers.models.User;
import com.cil.bf.gestion_courriers.repository.ProfilRepo;
import com.cil.bf.gestion_courriers.repository.UserRepo;
import com.cil.bf.gestion_courriers.services.interfaces.UserServiceInt;
import com.cil.bf.gestion_courriers.utils.Constants;
import com.cil.bf.gestion_courriers.utils.CourriersUtils;
import com.cil.bf.gestion_courriers.validator.UserValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserServiceInt {

    @Autowired
    UserRepo userRepository;

    @Autowired
    private ProfilRepo profilRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Enregistre un nouvel utilisateur dans le système.
     * 
     * @param userDto Les informations de l'utilisateur à enregistrer.
     * @return ResponseEntity avec un message de succès ou d'erreur.
     */
    @Override
    public ResponseEntity<String> save(UserDto userDto) {
        log.info("À l'intérieur de l'inscription\n" + " {}", userDto);
        try {
            // Valide les informations de l'utilisateur
            List<String> errors = UserValidator.valider(userDto);
            if (!errors.isEmpty()) {
                // Retourne une réponse avec un statut BAD_REQUEST si des erreurs sont présentes
                return CourriersUtils.getResponseEntity(Constants.DONNEES_INVALIDES, HttpStatus.BAD_REQUEST);
            }

            log.info("Utilisateur valide\n");

            // Vérifie si un utilisateur avec le même email ou matricule existe déjà
            if (userRepository.findByEmail(userDto.getEmail()) != null
                    || userRepository.findByMatricule(userDto.getMatricule()) != null) {
                // Retourne une réponse indiquant que l'utilisateur existe déjà
                return CourriersUtils.getResponseEntity("L'utilisateur existe déjà.", HttpStatus.CONFLICT);
            }

            // Enregistre le nouvel utilisateur
            User user = UserDto.toEntity(userDto);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(user);

            return CourriersUtils.getResponseEntity("Enregistré avec succès.", HttpStatus.CREATED);

        } catch (Exception e) {
            // Enregistre l'erreur dans le journal en cas d'exception
            log.error("Erreur lors de l'inscription de l'utilisateur\n\n", e);
            return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Met à jour les informations d'un utilisateur existant.
     * 
     * @param userDto Les informations de l'utilisateur à mettre à jour.
     * @return ResponseEntity avec un message de succès ou d'erreur.
     */
    @Override
    public ResponseEntity<String> update(UserDto userDto) {
        log.info("\nÀ l'intérieur de l'utilisateur\n\n" + " {}", userDto);

        try {
            log.info("Utilisateur valide\n\n");

            // Cherche l'utilisateur existant par son ID
            Optional<User> existeUserOpt = userRepository.findById(userDto.getId());
            if (!existeUserOpt.isPresent()) {
                // Retourne une réponse NOT_FOUND si l'utilisateur n'existe pas
                return CourriersUtils.getResponseEntity("Utilisateur non trouvé.", HttpStatus.NOT_FOUND);
            }

            User user = existeUserOpt.get();

            // Transforme la liste des profils DTO en entités Profil
            Set<Profil> profileList = userDto.getProfilDtoList().stream()
                    .map(ProfilDto::getId)
                    .map(profilRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());

            // Met à jour de l'utilisateur avec les nouvelles informations
            userDto.setPassword(user.getPassword());
            user = UserDto.toEntity(userDto);
            user.setProfilList(profileList);
            userRepository.save(user);

            log.info("Utilisateur modifié avec succes\n\n");
            return CourriersUtils.getResponseEntity("Enregistré avec succès.", HttpStatus.OK);

        } catch (Exception e) {
            // Enregistre l'erreur dans le journal en cas d'exception
            log.error("Erreur lors de la mise à jour de l'utilisateur\n\n", e);
            return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<List<UserDto>> findAll() {
        try {
            return new ResponseEntity<>(userRepository.findAll().stream()
                    .map(UserDto::fromEntity)
                    .collect(Collectors.toList()), HttpStatus.OK);
            // if (jwtFilter.isAdmin()) {
            // } else {
            // return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            // }
        } catch (Exception e) {
            // Enregistre l'erreur dans le journal en cas d'exception
            log.error("Erreur lors de la recherche de l'utilisateur\n\n", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> login(AuthDto authDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    @Override
    public ResponseEntity<List<ProfilDto>> findAllProfil(Long id) {
        try {
            return new ResponseEntity<>(userRepository.findAllProfil(id).stream()
                    .map(ProfilDto::fromEntity)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            // Enregistre l'erreur dans le journal en cas d'exception
            log.error("Erreur lors de la recherche des profils de l'utilisateur\n\n", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @Override
    public ResponseEntity<List<PrivilegeDto>> findAllPrivilege(Long id) {
        try {
            return new ResponseEntity<>(userRepository.findAllPrivilege(id).stream()
                    .map(PrivilegeDto::fromEntity)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            // Enregistre l'erreur dans le journal en cas d'exception
            log.error("Erreur lors de la recherche des privilèges de l'utilisateur\n\n", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}