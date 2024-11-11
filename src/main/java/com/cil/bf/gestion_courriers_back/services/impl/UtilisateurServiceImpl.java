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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cil.bf.gestion_courriers_back.Auth.dto.AuthDto;
import com.cil.bf.gestion_courriers_back.Auth.jwt.JWTServiceInt;
import com.cil.bf.gestion_courriers_back.Auth.service.Impl.CustomerUserDetailsService;
import com.cil.bf.gestion_courriers_back.dto.PrivilegeDto;
import com.cil.bf.gestion_courriers_back.dto.ProfilDto;
import com.cil.bf.gestion_courriers_back.dto.ResquestIdDto;
import com.cil.bf.gestion_courriers_back.dto.UtilisateurDto;
import com.cil.bf.gestion_courriers_back.models.Profils;
import com.cil.bf.gestion_courriers_back.models.Utilisateurs;
import com.cil.bf.gestion_courriers_back.repository.ProfilRepo;
import com.cil.bf.gestion_courriers_back.repository.UtilisateurRepo;
import com.cil.bf.gestion_courriers_back.services.interfaces.UtilsateurServiceInt;
import com.cil.bf.gestion_courriers_back.utils.Constants;
import com.cil.bf.gestion_courriers_back.utils.CourriersUtils;
import com.cil.bf.gestion_courriers_back.validator.UtilisateurValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UtilisateurServiceImpl implements UtilsateurServiceInt {

    @Autowired
    UtilisateurRepo utilisateurRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    JWTServiceInt jwtServiceInt;

    @Autowired
    private ProfilRepo profilRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    Utilisateurs utilisateur;

    /**
     * Enregistre un nouvel utilisateur dans le système.
     * 
     * @param utilisateurDto Les informations de l'utilisateur à enregistrer.
     * @return ResponseEntity avec un message de succès ou d'erreur.
     */
    @Override
    public ResponseEntity<String> save(UtilisateurDto utilisateurDto) {
        log.info("À l'intérieur de l'inscription\n" + " {}", utilisateurDto);
        try {
            // Valide les informations de l'utilisateur
            List<String> errors = UtilisateurValidator.valider(utilisateurDto);
            if (!errors.isEmpty()) {
                // Retourne une réponse avec un statut BAD_REQUEST si des erreurs sont présentes
                log.info(String.join("\n", errors));
                return CourriersUtils.getResponseEntity("\n" + String.join(",\n", errors) + "\n",
                        HttpStatus.BAD_REQUEST);
            }
            log.info("Formulaire Utilisateur valide\n");

            // Verifie si les profils existe
            if (!verifyAllProfilesExist(utilisateurDto.getProfilDtoList()).isEmpty()) {
                // Le profil n'existe pas
                log.info("\n" + String.join(",\n", verifyAllProfilesExist(utilisateurDto.getProfilDtoList())) + "\n");
                return CourriersUtils.getResponseEntity(
                        "\n" + String.join(",\n", verifyAllProfilesExist(utilisateurDto.getProfilDtoList())) + "\n",
                        HttpStatus.NOT_FOUND);
            }

            // Vérifie si un utilisateur avec le même email ou matricule existe déjà
            if (utilisateurRepo.findByEmail(utilisateurDto.getEmail()).isPresent()
                    || utilisateurRepo.findByMatricule(utilisateurDto.getMatricule()).isPresent()) {
                // Retourne une réponse indiquant que l'utilisateur existe déjà
                log.info("Utilisateur existe déjà " + "{} ou {}\n\n", utilisateurDto.getEmail(),
                        utilisateurDto.getMatricule());
                return CourriersUtils.getResponseEntity(
                        "L'utilisateur existe déjà avec " + utilisateurDto.getEmail() + " ou "
                                + utilisateurDto.getMatricule() + "",
                        HttpStatus.UNAUTHORIZED);
            }

            // Enregistre le nouvel utilisateur
            Utilisateurs utilisateur = UtilisateurDto.toEntity(utilisateurDto);
            utilisateur.setPassword(passwordEncoder.encode(utilisateurDto.getPassword()));
            utilisateur.setAccountNonExpired(false);
            utilisateur.setAccountNonLocked(false);
            utilisateur.setEnabled(false);
            utilisateurRepo.save(utilisateur);
            log.info("Utilisateur enregistré avec succès.\n\n");
            return CourriersUtils.getResponseEntity("Enregistré avec succès.", HttpStatus.CREATED);

        } catch (Exception e) {
            // Enregistre l'erreur dans le journal en cas d'exception
            log.error("Erreur lors de l'inscription de l'utilisateur.\n\n" + "{}", e);
            return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public List<String> verifyAllProfilesExist(Set<ProfilDto> set) {
        List<String> error = new ArrayList<>();
        if (set == null) {
            error.add("La liste des profil est vide");
            return error;
        }
        Integer i = 0;
        for (ProfilDto profilDto : set) {
            i++;
            if (!profilRepository.existsById(profilDto.getId())) {
                if (i == 1) {
                    error.add("Le " + i + "er profil n'existe pas");
                } else {
                    error.add("Le " + i + "ème profil n'existe pas");

                }
            }
        }
        return error; // Le profil n'existe pas
    }

    /**
     * Met à jour les informations d'un utilisateur
     * 
     * @param utilisateurDto Les informations de l'utilisateur à mettre à jour.
     * @return ResponseEntity avec un message de succès ou d'erreur.
     */
    @Override
    public ResponseEntity<String> update(UtilisateurDto utilisateurDto) {
        log.info("\nÀ l'intérieur de l'utilisateur\n\n" + " {}", utilisateurDto);

        try {
            // Valide les informations de l'utilisateur
            List<String> errors = UtilisateurValidator.valider(utilisateurDto);
            if (!errors.isEmpty()) {
                // Retourne une réponse avec un statut BAD_REQUEST si des erreurs sont présentes
                log.info(String.join("\n", errors));
                return CourriersUtils.getResponseEntity("\n" + String.join(",\n", errors) + "\n",
                        HttpStatus.BAD_REQUEST);
            }
            log.info("Formulaire Utilisateur valide\n\n");

            // Verifie si les profils existe
            if (!verifyAllProfilesExist(utilisateurDto.getProfilDtoList()).isEmpty()) {
                // Le profil n'existe pas
                log.info("\n" + String.join(",\n", verifyAllProfilesExist(utilisateurDto.getProfilDtoList())) + "\n");
                return CourriersUtils.getResponseEntity(
                        "\n" + String.join(",\n", verifyAllProfilesExist(utilisateurDto.getProfilDtoList())) + "\n",
                        HttpStatus.NOT_FOUND);
            }

            // Cherche l'utilisateur existant par son ID
            Optional<Utilisateurs> existeutilisateurOpt = utilisateurRepo.findById(utilisateurDto.getId());
            if (!existeutilisateurOpt.isPresent()) {
                // Retourne une réponse NOT_FOUND si l'utilisateur n'existe pas
                log.error("Utilisateur non trouvé.\n\n");
                return CourriersUtils.getResponseEntity("Utilisateur non trouvé.", HttpStatus.NOT_FOUND);
            }

            Utilisateurs utilisateur = existeutilisateurOpt.get();

            // Transforme la liste des profils DTO en entités Profil
            Set<Profils> profileList = utilisateurDto.getProfilDtoList().stream()
                    .map(ProfilDto::getId)
                    .map(profilRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());

            // Met à jour de l'utilisateur avec les nouvelles informations
            // utilisateurDto.setPassword(utilisateur.getPassword());
            utilisateur = UtilisateurDto.toEntity(utilisateurDto);
            utilisateur.setPassword(passwordEncoder.encode(utilisateurDto.getPassword()));
            utilisateur.setProfilList(profileList);
            utilisateurRepo.save(utilisateur);

            log.info("Utilisateur modifié avec succes\n\n");
            return CourriersUtils.getResponseEntity("Enregistré avec succès.", HttpStatus.OK);

        } catch (Exception e) {
            // Enregistre l'erreur dans le journal en cas d'exception
            log.error("Erreur lors de la mise à jour de l'utilisateur\n\n" + "{}", e);
            return CourriersUtils.getResponseEntity(Constants.QUELQUE_CHOSE_S_EST_PASSE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Récupère tous les utilisateurs de la base de données et les renvoie
     * sousString
     * forme de liste de {@link UtilisateurDto}.
     *
     * Cette méthode tente de récupérer tous les utilisateurs du référentiel
     * utilisateur, les mappe
     * en objets {@link UtilisateurDto} et les renvoie sous forme de
     * {@link ResponseEntity}
     * avec un statut HTTP de 200 (OK).
     * En cas d'exception, elle renvoie une réponse vide avec un statut HTTP de 500
     * (INTERNAL_SERVER_ERROR).
     *
     * @return Une instance de {@link ResponseEntity} contenant la liste des
     *         utilisateurs sous forme de {@link UtilisateurDto}
     *         et un statut HTTP de 200 (OK) si réussi, ou une réponse vide avec un
     *         statut HTTP de 500 (INTERNAL_SERVER_ERROR) en cas d'erreur.
     */
    @Override
    public ResponseEntity<List<UtilisateurDto>> findAll() {
        try {
            List<UtilisateurDto> utilisateurDtos = utilisateurRepo.findAll().stream()
                    .map(UtilisateurDto::fromEntity)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(utilisateurDtos, HttpStatus.OK);
        } catch (Exception e) {
            // Enregistre l'erreur dans le journal en cas d'exception
            log.error("Erreur lors de la recherche des utilisateurs", e);
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Recherche des profils existant d'un Utilisateur.
     * 
     * @return ResponseEntity avec la liste des profils de l'utilisateur ou un
     *         message d'erreur.
     */
    @Override
    public ResponseEntity<List<ProfilDto>> findAllProfil(ResquestIdDto resquestIdDto) {
        try {

            return new ResponseEntity<>(utilisateurRepo.findAllProfil(resquestIdDto.getId()).stream()
                    .map(ProfilDto::fromEntity)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            // Enregistre l'erreur dans le journal en cas d'exception
            log.error("Erreur lors de la recherche des profils de l'utilisateur\n\n", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    /**
     * Recherche des privileges existant d'un Utilisateur.
     * 
     * @return ResponseEntity avec la liste des privileges de l'utilisateur ou un
     *         message d'erreur.
     */

    @Override
    public ResponseEntity<List<PrivilegeDto>> findAllPrivilege(ResquestIdDto resquestIdDto) {
        try {

            // Cherche l'utilisateur existant par son email
            Optional<Utilisateurs> existeutilisateurOpt = utilisateurRepo.findById(resquestIdDto.getId());

            // Si l'utilisateur n'existe pas, journalise une erreur et lance une exception
            if (!existeutilisateurOpt.isPresent()) {
                log.error("Utilisateur non trouvé : " + resquestIdDto.getId());
                throw new UsernameNotFoundException("Utilisateur " + resquestIdDto.getId() + " non trouvé.");
            }

            // Récupère l'objet utilisateur à partir de l'Optional
            utilisateur = existeutilisateurOpt.get();
            UtilisateurDto utilisateurDto = UtilisateurDto.fromEntity(utilisateur);

            // Récupère la liste des privilèges de l'utilisateur

            List<PrivilegeDto> privilegeList = new ArrayList<>(utilisateurDto.getPrivilegeDtoList());
            return new ResponseEntity<>(privilegeList, HttpStatus.OK);
        } catch (Exception e) {
            // Enregistre l'erreur dans le journal en cas d'exception
            log.error("Erreur lors de la recherche des privilèges de l'utilisateur\n\n", e);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<UtilisateurDto> findByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByEmail'");
    }

    @Override
    public ResponseEntity<UtilisateurDto> findByLogin(String login) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByLogin'");
    }

    @Override
    public ResponseEntity<UtilisateurDto> findByMatricule(String matricule) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByMatricule'");
    }

    @Override
    public Boolean isUserGood(AuthDto authRequest) {

        Optional<Utilisateurs> optionalutilisateur = utilisateurRepo.findByMatricule(authRequest.getMatricule());

        if (optionalutilisateur.isPresent()) {
            String currentEncryptedPassword = optionalutilisateur.get().getPassword();
            if (passwordEncoder.matches(authRequest.getPassword(), currentEncryptedPassword)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean isUserActif(AuthDto authRequest) {
        Optional<Utilisateurs> optionalutilisateur = utilisateurRepo.findByMatricule(authRequest.getMatricule());
        if (optionalutilisateur.isPresent()) {
            return optionalutilisateur.get().isEnabled();
        } else {
            return false;
        }
    }

}