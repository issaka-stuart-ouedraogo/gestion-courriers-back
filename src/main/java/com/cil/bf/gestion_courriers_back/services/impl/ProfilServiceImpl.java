package com.cil.bf.gestion_courriers_back.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cil.bf.gestion_courriers_back.dto.PrivilegeDto;
import com.cil.bf.gestion_courriers_back.dto.ProfilDto;
import com.cil.bf.gestion_courriers_back.models.Privileges;
import com.cil.bf.gestion_courriers_back.models.Profils;
import com.cil.bf.gestion_courriers_back.repository.PrivilegeRepo;
import com.cil.bf.gestion_courriers_back.repository.ProfilRepo;
import com.cil.bf.gestion_courriers_back.repository.UtilisateurRepo;
import com.cil.bf.gestion_courriers_back.services.interfaces.ProfilServiceInt;
import com.cil.bf.gestion_courriers_back.utils.Constants;
import com.cil.bf.gestion_courriers_back.utils.CourriersUtils;
import com.cil.bf.gestion_courriers_back.validator.ProfilValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProfilServiceImpl implements ProfilServiceInt {

    @Autowired
    ProfilRepo profilRepository;

    @Autowired
    PrivilegeRepo privilegeRepository;

    @Autowired
    UtilisateurRepo userRepository;

    /**
     * Enregistre un nouveau profil dans le système.
     *
     * @param profilDto Les informations du profil à enregistrer.
     * @return ResponseEntity avec un message de succès ou d'erreur.
     */
    @Override
    public ResponseEntity<String> save(ProfilDto profilDto) {
        log.info("Enregistrement du profil : {}", profilDto);

        try {
            // Validation des informations du profil
            List<String> errors = ProfilValidator.valider(profilDto);
            if (!errors.isEmpty()) {
                log.info("Erreurs de validation : {}", String.join(", ", errors));
                return CourriersUtils.getResponseEntity(String.join(",\n", errors), HttpStatus.BAD_REQUEST);
            }

            // Vérifie si les privilèges existent
            List<String> privilegeErrors = verifyAllPrivilegesExist(profilDto.getPrivilegeDtoList());
            if (!privilegeErrors.isEmpty()) {
                log.info("Privilèges manquants : {}", String.join(", ", privilegeErrors));
                return CourriersUtils.getResponseEntity(String.join(",\n", privilegeErrors), HttpStatus.NOT_FOUND);
            }

            // Vérifie si le profil existe déjà par code ou libellé
            if (profilRepository.findByCode(profilDto.getCode()) != null ||
                    profilRepository.findByLibelle(profilDto.getLibelle()) != null) {
                log.info("Profil existe déjà avec ce code ou libellé.");
                return CourriersUtils.getResponseEntity("Profil existe déjà.", HttpStatus.CONFLICT);
            }

            // Mise à jour des privilèges
            Set<Privileges> privileges = profilDto.getPrivilegeDtoList().stream()
                    .map(PrivilegeDto::getId)
                    .map(privilegeRepository::findById)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toSet());

            // Enregistre le profil
            Profils profil = new Profils();
            profil = ProfilDto.toEntity(profilDto);
            profil.setPrivilegeList(privileges);

            profilRepository.save(profil);
            log.info("Profil enregistré avec succès.");
            return CourriersUtils.getResponseEntity("Profil enregistré avec succès.", HttpStatus.OK);

        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement du profil : {}", e.getMessage(), e);
            return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Vérifie l'existence de tous les privilèges dans la liste fournie.
     *
     * @param privileges Set de privilèges à vérifier.
     * @return Liste d'erreurs si certains privilèges n'existent pas.
     */
    public List<String> verifyAllPrivilegesExist(Set<PrivilegeDto> privileges) {
        List<String> errors = new ArrayList<>();
        if (privileges == null || privileges.isEmpty()) {
            errors.add("La liste des privilèges est vide.");
        } else {
            int index = 1;
            for (PrivilegeDto privilegeDto : privileges) {
                if (!profilRepository.existsById(privilegeDto.getId())) {
                    errors.add(String.format("Le privilège avec ID = " + privilegeDto.getId() + " n'existe pas.", index,
                            privilegeDto.getId()));
                }
                index++;
            }
        }
        return errors;
    }

    /**
     * Met à jour les informations d'un profil existant.
     *
     * @param profilDto Les informations du profil à mettre à jour.
     * @return ResponseEntity avec un message de succès ou d'erreur.
     */
    @Override
    public ResponseEntity<String> update(ProfilDto profilDto) {
        log.info("Mise à jour du profil : {}", profilDto);

        try {
            // Validation du profil
            List<String> errors = ProfilValidator.valider(profilDto);
            if (!errors.isEmpty()) {
                log.info("Erreurs de validation : {}", String.join(", ", errors));
                return CourriersUtils.getResponseEntity(String.join(",\n", errors), HttpStatus.BAD_REQUEST);
            }

            // Vérification des privilèges
            List<String> privilegeErrors = verifyAllPrivilegesExist(profilDto.getPrivilegeDtoList());
            if (!privilegeErrors.isEmpty()) {
                log.info("Privilèges inexistants : {}", String.join(", ", privilegeErrors));
                return CourriersUtils.getResponseEntity(String.join(",\n", privilegeErrors), HttpStatus.NOT_FOUND);
            }

            // Recherche et vérification du profil existant
            Optional<Profils> profileOpt = profilRepository.findById(profilDto.getId());
            if (profileOpt.isEmpty()) {
                log.info("Profil non trouvé avec l'ID : {}", profilDto.getId());
                return CourriersUtils.getResponseEntity("Profil non trouvé.", HttpStatus.NOT_FOUND);
            }

            // Vérifie si le profil existe déjà par code ou libellé
            Profils profilByCode = profilRepository.findByCode(profilDto.getCode());
            Profils profilByLibelle = profilRepository.findByLibelle(profilDto.getLibelle());

            Profils profil = profileOpt.get();

            if ((profilByCode != null && !profilByCode.getId().equals(profil.getId())) ||
                    (profilByLibelle != null && !profilByLibelle.getId().equals(profil.getId()))) {
                log.info("Profil existe déjà avec ce code ou libellé.");
                return CourriersUtils.getResponseEntity("Profil existe déjà.", HttpStatus.CONFLICT);
            }

            // Mise à jour du profil
            Set<Privileges> privileges = profilDto.getPrivilegeDtoList().stream()
                    .map(PrivilegeDto::getId)
                    .map(privilegeRepository::findById)
                    .flatMap(Optional::stream)
                    .collect(Collectors.toSet());

            profil = ProfilDto.toEntity(profilDto);
            profil.setPrivilegeList(privileges);
            profilRepository.save(profil);

            log.info("Profil mis à jour avec succès.");
            return CourriersUtils.getResponseEntity("Enregistré avec succès.", HttpStatus.OK);

        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour du profil : {}", e.getMessage(), e);
            return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Supprime un profil existant.
     *
     * @param id L'identifiant du profil à supprimer.
     * @return ResponseEntity avec un message de succès ou d'erreur.
     */
    @Override
    @Transactional
    public ResponseEntity<String> delete(Long id) {
        log.info("Suppression du profil avec ID : {}", id);

        try {
            Optional<Profils> profilOpt = profilRepository.findById(id);
            if (profilOpt.isEmpty()) {
                log.info("Profil non trouvé avec ID : {}", id);
                return CourriersUtils.getResponseEntity("Le profil n'existe pas.", HttpStatus.NOT_FOUND);
            }

            Profils profil = profilOpt.get();

            if (!profil.getPrivilegeList().isEmpty()) {
                log.info("Profil associé à des privilèges, suppression impossible.");
                return CourriersUtils.getResponseEntity("Le profil est associé à des privilèges.", HttpStatus.CONFLICT);
            }

            if (!userRepository.findByProfilListId(profil.getId()).isEmpty()) {
                log.info("Profil associé à des utilisateurs, suppression impossible.");
                return CourriersUtils.getResponseEntity("Le profil est associé à des utilisateurs.",
                        HttpStatus.CONFLICT);
            }

            profilRepository.delete(profil);
            log.info("Profil supprimé avec succès.");
            return CourriersUtils.getResponseEntity("Profil supprimé avec succès.", HttpStatus.OK);

        } catch (Exception e) {
            log.error("Erreur lors de la suppression du profil : {}", e.getMessage(), e);
            return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Recherche tous les profils.
     *
     * @return ResponseEntity avec la liste des profils.
     */
    @Override
    public ResponseEntity<List<ProfilDto>> findAll() {
        log.info("Recherche de tous les profils.");

        try {
            List<ProfilDto> profils = profilRepository.findAll().stream()
                    .map(ProfilDto::fromEntity)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(profils, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Erreur lors de la recherche des profils : {}", e.getMessage(), e);
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Vérifie si un profil est dupliqué par son code ou son libellé.
     *
     * @param profilDto Le DTO du profil à vérifier.
     * @param profile   Le profil existant.
     * @return true si un profil est dupliqué, sinon false.
     */
    private boolean isDuplicateProfil(ProfilDto profilDto, Profils profile) {
        Profils profilByCode = profilRepository.findByCode(profilDto.getCode());
        Profils profilByLibelle = profilRepository.findByLibelle(profilDto.getLibelle());
        return (profilByCode != null && !profile.getId().equals(profilByCode.getId())) ||
                (profilByLibelle != null && !profile.getId().equals(profilByLibelle.getId()));
    }

}
