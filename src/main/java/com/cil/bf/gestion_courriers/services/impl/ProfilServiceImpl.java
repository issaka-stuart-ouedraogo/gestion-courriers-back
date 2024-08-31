package com.cil.bf.gestion_courriers.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cil.bf.gestion_courriers.dto.PrivilegeDto;
import com.cil.bf.gestion_courriers.dto.ProfilDto;
import com.cil.bf.gestion_courriers.models.Privilege;
import com.cil.bf.gestion_courriers.models.Profil;
import com.cil.bf.gestion_courriers.repository.PrivilegeRepo;
import com.cil.bf.gestion_courriers.repository.ProfilRepo;
import com.cil.bf.gestion_courriers.services.interfaces.ProfilServiceInt;
import com.cil.bf.gestion_courriers.utils.Constants;
import com.cil.bf.gestion_courriers.utils.CourriersUtils;
import com.cil.bf.gestion_courriers.validator.ProfilValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProfilServiceImpl implements ProfilServiceInt {

    @Autowired
    ProfilRepo profilRepository;

    @Autowired
    PrivilegeRepo privilegeRepository;

    /**
     * Enregistre un nouvel profil dans le système.
     * 
     * @param profilDto Les informations du profil à enregistrer.
     * @return ResponseEntity avec un message de succès ou d'erreur.
     */
    @Override
    public ResponseEntity<String> save(ProfilDto profilDto) {
        log.info("À l'intérieur de l'enregistrement\n" + " {}", profilDto);
        try {
            // Valide les informations du profil
            List<String> errors = ProfilValidator.valider(profilDto);
            if (!errors.isEmpty()) {
                // Retourne une réponse avec un statut BAD_REQUEST si des erreurs sont présentes
                return CourriersUtils.getResponseEntity(errors.get(0),
                        HttpStatus.BAD_REQUEST);
            }
            log.info("Profil valide\n");

            // Si le profil existe déjà par code ou libellé, retourne une réponse NOT_FOUND
            if (profilRepository.findByCode(profilDto.getCode()) != null
                    || profilRepository.findByLibelle(profilDto.getLibelle()) != null) {
                return CourriersUtils.getResponseEntity("Profil existe déjà.", HttpStatus.CONFLICT);
            }

            // Enregistre le profil dans le repository
            profilRepository.save(ProfilDto.toEntity(profilDto));

            return CourriersUtils.getResponseEntity("Profil enregistré avec succès.", HttpStatus.OK);

        } catch (Exception e) {
            // Enregistre l'erreur dans le journal en cas d'exception
            log.error("Erreur lors de l'enregistrement du profil\n\n", e);
            return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Met à jour les informations d'un profil existant.
     * 
     * @param userDto Les informations du profil à mettre à jour.
     * @return ResponseEntity avec un message de succès ou d'erreur.
     */
    @Override
    public ResponseEntity<String> update(ProfilDto profilDto) {
        log.info("\nÀ l'intérieur du profil\n\n" + " {}", profilDto);

        try {
            log.info("Profil valide\n\n");

            // Cherche le profil existant par son ID, par Libelle et Code.
            Optional<Profil> existeProfilByIdOpt = profilRepository.findById(profilDto.getId());
            Profil existeProfilByLibelle = profilRepository.findByLibelle(profilDto.getLibelle());
            Profil existeProfilByCode = profilRepository.findByCode(profilDto.getCode());

            if (!existeProfilByIdOpt.isPresent()) {
                // Retourne une réponse NOT_FOUND si le profil n'existe pas
                return CourriersUtils.getResponseEntity("Profil non trouvé.", HttpStatus.NOT_FOUND);
            }

            Profil profil = existeProfilByIdOpt.get();

            // Vérifie si un profil avec le même libelle ou code existe déjà, sauf s'il
            // s'agit du même profil
            if ((existeProfilByCode != null && !profil.getId().equals(existeProfilByCode.getId())) ||
                    (existeProfilByLibelle != null && !profil.getId().equals(existeProfilByLibelle.getId()))) {
                // Retourne une réponse indiquant que le profil existe déjà
                return CourriersUtils.getResponseEntity("Le profil existe déjà.", HttpStatus.CONFLICT);
            }

            // Transforme la liste des privileges DTO en entités privileges
            Set<Privilege> privilegeList = profilDto.getPrivilegeDtoList().stream()
                    .map(PrivilegeDto::getId)
                    .map(privilegeRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());

            // Met à jour le profil avec les nouvelles informations
            profil = ProfilDto.toEntity(profilDto);
            profil.setPrivilegeList(privilegeList);
            profilRepository.save(profil);

            log.info("Profil modifié avec succes\n\n");
            return CourriersUtils.getResponseEntity("Enregistré avec succès.", HttpStatus.OK);

        } catch (Exception e) {
            // Enregistre l'erreur dans le journal en cas d'exception
            log.error("Erreur lors de la mise à jour de l'utilisateur\n\n", e);
            return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    @Transactional // Assure que les opérations sont effectuées dans une transaction
    public ResponseEntity<String> delete(Long id) {

        try {
            // Vérifiez d'abord si le profil existe
            Optional<Profil> profilOpt = profilRepository.findById(id);
            if (!profilOpt.isPresent()) {
                // Retourne une réponse indiquant que le profil existe déjà
                return CourriersUtils.getResponseEntity("Le profil n'existe pas.", HttpStatus.NOT_FOUND);
            }

            Profil profil = profilOpt.get();
            // Vérifie si le Profil est associé à des Privileges
            if (!profil.getPrivilegeList().isEmpty()) {
                // Message d'erreur si des Privileges sont associés
                return CourriersUtils.getResponseEntity(
                        "Impossible de supprimer le profil avec les privilèges associés",
                        HttpStatus.INTERNAL_SERVER_ERROR);

            }
            // Supprimez le profil; cela supprime également les associations avec les
            // privilèges
            profilRepository.delete(profil);
            return CourriersUtils.getResponseEntity("Profil supprimé",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            // Enregistre l'erreur dans le journal en cas d'exception
            log.error("Erreur lors de la mise à jour de l'utilisateur\n\n", e);
        }
        return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProfilDto>> findAll() {
        try {
            return new ResponseEntity<>(profilRepository.findAll().stream()
                    .map(ProfilDto::fromEntity)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            // Enregistre l'erreur dans le journal en cas d'exception
            log.error("Erreur lors de la recherche des profils\n\n", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
