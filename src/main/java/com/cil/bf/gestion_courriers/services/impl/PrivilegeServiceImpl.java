package com.cil.bf.gestion_courriers.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cil.bf.gestion_courriers.dto.PrivilegeDto;
import com.cil.bf.gestion_courriers.models.Privilege;
import com.cil.bf.gestion_courriers.repository.PrivilegeRepo;
import com.cil.bf.gestion_courriers.repository.ProfilRepo;
import com.cil.bf.gestion_courriers.services.interfaces.PrivilegeServiceInt;
import com.cil.bf.gestion_courriers.utils.Constants;
import com.cil.bf.gestion_courriers.utils.CourriersUtils;
import com.cil.bf.gestion_courriers.validator.PrivilegeValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PrivilegeServiceImpl implements PrivilegeServiceInt {
    @Autowired
    PrivilegeRepo privilegeRepository;

    @Autowired
    ProfilRepo profilRepository;

    /**
     * Enregistre un nouveau privilège dans le système.
     * 
     * @param userDto Les informations du privilège à enregistrer.
     * @return ResponseEntity avec un message de succès ou d'erreur.
     */
    @Override
    public ResponseEntity<String> save(PrivilegeDto privilegeDto) {
        log.info("À l'intérieur de l'inscription\n" + " {}", privilegeDto);
        try {
            // Valide les informations du privilège
            List<String> errors = PrivilegeValidator.valider(privilegeDto);
            if (!errors.isEmpty()) {
                // Retourne une réponse avec un statut BAD_REQUEST si des erreurs sont présentes
                return CourriersUtils.getResponseEntity(errors.get(0),
                        HttpStatus.BAD_REQUEST);
            }
            log.info("Privilege valide\n");

            // Vérifie si un privilege avec le même code ou libelle existe déjà
            if (privilegeRepository.findByCode(privilegeDto.getCode()) != null
                    || privilegeRepository.findByLibelle(privilegeDto.getLibelle()) != null) {

                // Retourne une réponse indiquant que le privilege existe déjà
                return CourriersUtils.getResponseEntity("Privilege existe déjà.", HttpStatus.CONFLICT);
            }

            // Enregistre le privilège dans le bdd
            privilegeRepository.save(PrivilegeDto.toEntity(privilegeDto));

            return CourriersUtils.getResponseEntity("Privileges enregistré avec succès.", HttpStatus.OK);

        } catch (Exception e) {
            // Enregistre l'erreur dans le journal en cas d'exception
            log.error("Erreur lors de l'inscription de le privilège\n\n", e);
            e.fillInStackTrace();
            return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> update(PrivilegeDto privilegeDto) {
        log.info("\nÀ l'intérieur du privilege\n\n" + " {}", privilegeDto);

        try {
            log.info("privilege valide\n\n");

            // Met le libelle et le code en majuscule
            privilegeDto.setLibelle(privilegeDto.getLibelle().toUpperCase());
            privilegeDto.setCode(privilegeDto.getCode().toUpperCase());
            // Cherche le privilege existant par son ID, par Libelle et Code.
            Optional<Privilege> existePrivilegeByIdOpt = privilegeRepository.findById(privilegeDto.getId());
            Privilege existePrivilegeByLibelle = privilegeRepository.findByLibelle(privilegeDto.getLibelle());
            Privilege existePrivilegeByCode = privilegeRepository.findByCode(privilegeDto.getCode());

            if (!existePrivilegeByIdOpt.isPresent()) {
                // Retourne une réponse NOT_FOUND si le privilege n'existe pas
                return CourriersUtils.getResponseEntity("Privilege non trouvé.", HttpStatus.NOT_FOUND);
            }

            Privilege privilege = existePrivilegeByIdOpt.get();

            // Vérifie si un privilege avec le même libelle ou code existe déjà
            if ((existePrivilegeByLibelle != null && !privilege.getId().equals(existePrivilegeByLibelle.getId())) ||
                    (existePrivilegeByCode != null && !privilege.getId().equals(existePrivilegeByCode.getId()))) {
                // Retourne une réponse indiquant que le privilege existe déjà
                return CourriersUtils.getResponseEntity("Le privilege existe déjà.", HttpStatus.CONFLICT);
            }

            // Met à jour du privilege avec les nouvelles informations
            privilegeRepository.save(PrivilegeDto.toEntity(privilegeDto));

            log.info("Privileges modifié avec succes\n\n");
            return CourriersUtils.getResponseEntity("Privileges modifié avec succes.", HttpStatus.OK);

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
            Optional<Privilege> privilegeOpt = privilegeRepository.findById(id);
            if (!privilegeOpt.isPresent()) {
                // Retourne une réponse indiquant que le privilege n'existe pas
                return CourriersUtils.getResponseEntity("Le privilege n'existe pas.", HttpStatus.NOT_FOUND);
            }

            Privilege privilege = privilegeOpt.get();

            // Vérifie si le Privilege est associé à des Profils
            if (!profilRepository.findByPrivilegeListId(privilege.getId()).isEmpty()) {
                // Message d'erreur si des Profils sont associés
                return CourriersUtils.getResponseEntity(
                        "Impossible de supprimer le privilège car il est associé à un ou plusieurs profils",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Supprimez le profil; cela supprime également les associations avec les
            // privilèges
            privilegeRepository.delete(privilege);
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
    public ResponseEntity<List<PrivilegeDto>> findAll() {
        try {
            return new ResponseEntity<>(privilegeRepository.findAll().stream()
                    .map(PrivilegeDto::fromEntity)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            // Enregistre l'erreur dans le journal en cas d'exception
            log.error("Erreur lors de la recherche des privilège\n\n", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
