package com.cil.bf.gestion_courriers_back.Auth.service.Impl;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cil.bf.gestion_courriers_back.Auth.dto.AuthDto;
import com.cil.bf.gestion_courriers_back.Auth.dto.JwtAuthResponseDto;
import com.cil.bf.gestion_courriers_back.Auth.dto.RefreshTokenDto;
import com.cil.bf.gestion_courriers_back.Auth.dto.ResetPasswordDto;
import com.cil.bf.gestion_courriers_back.Auth.jwt.JWTServiceInt;
import com.cil.bf.gestion_courriers_back.Auth.service.interfaces.AuthServiceInt;
import com.cil.bf.gestion_courriers_back.models.Utilisateurs;
import com.cil.bf.gestion_courriers_back.repository.UtilisateurRepo;
import com.cil.bf.gestion_courriers_back.utils.CourriersUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthServiceImpl implements AuthServiceInt {

    @Autowired
    UtilisateurRepo userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    JWTServiceInt jwtService;

    @Autowired
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<String> enableAccount(String token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'enableAccount'");
    }

    /**
     * Authentifie un utilisateur en fonction des informations fournies dans l'objet
     * AuthDto.
     *
     * Cette méthode tente de valider l'identité de l'utilisateur en utilisant
     * son matricule et son mot de passe. Si l'authentification réussit, un token
     * JWT
     * est généré et renvoyé dans la réponse, accompagné d'un token de
     * rafraîchissement.
     * En cas d'échec, un message d'erreur approprié est renvoyé avec le statut HTTP
     * correspondant.
     *
     * @param authDto Un objet contenant les informations d'authentification,
     *                y compris le matricule et le mot de passe.
     * @return ResponseEntity contenant le token JWT et le token de rafraîchissement
     *         si l'authentification réussit, ou un message d'erreur et un statut
     *         HTTP approprié si elle échoue.
     *         Les statuts HTTP possibles sont :
     *         - 200 OK : Authentification réussie.
     *         - 401 UNAUTHORIZED : Identifiants incorrects.
     *         - 423 LOCKED : Compte utilisateur verrouillé.
     *         - 403 FORBIDDEN : Compte utilisateur désactivé.
     *         - 500 INTERNAL SERVER ERROR : Erreur interne inattendue.
     */

    @Override
    public ResponseEntity<?> signIn(AuthDto authDto) {
        log.info("À l'intérieur de la connexion {}", authDto);

        try {
            // Tente d'authentifier l'utilisateur avec le gestionnaire d'authentification
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDto.getMatricule(), authDto.getPassword()));

            // Charge les détails de l'utilisateur pour générer un token JWT
            final UserDetails userDetails = customerUserDetailsService.loadUserByUsername(authDto.getMatricule());

            var jwt = jwtService.generateAccessToken(userDetails);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), userDetails);

            JwtAuthResponseDto jwtAuthResponseDto = new JwtAuthResponseDto();
            jwtAuthResponseDto.setAccessToken(jwt);
            jwtAuthResponseDto.setRefreshToken(refreshToken);

            // Renvoie le token JWT si tout est en ordre
            return new ResponseEntity<JwtAuthResponseDto>(jwtAuthResponseDto, HttpStatus.OK);

        } catch (LockedException e) {
            // Gestion des comptes verrouillés
            log.warn("Le compte utilisateur est verrouillé : {}", authDto.getMatricule());
            return CourriersUtils.getResponseEntity("Le compte est verrouillé. Veuillez contacter l'administrateur.",
                    HttpStatus.LOCKED);

        } catch (DisabledException e) {
            // Gestion des comptes désactivés
            log.warn("Le compte utilisateur est désactivé : {}", authDto.getMatricule());
            return CourriersUtils.getResponseEntity("Le compte est désactivé. Veuillez contacter l'administrateur.",
                    HttpStatus.FORBIDDEN);

        } catch (BadCredentialsException e) {
            // Gestion des échecs d'authentification dus à des identifiants incorrects
            log.warn(
                    "Échec de l'authentification en raison d'informations d'identification incorrectes pour l'utilisateur : {}",
                    authDto.getMatricule());
            return CourriersUtils.getResponseEntity("Les informations d'authentification sont erronées!",
                    HttpStatus.UNAUTHORIZED);

        } catch (CredentialsExpiredException e) {
            log.warn("Les informations d'identification de l'utilisateur sont expirées : {}", authDto.getMatricule());
            return CourriersUtils.getResponseEntity(
                    "Les informations d'identification sont expirées. Veuillez mettre à jour votre mot de passe.",
                    HttpStatus.FORBIDDEN);

        } catch (AccountExpiredException e) {
            // Gestion des comptes expirés
            log.warn("Le compte utilisateur est expiré : {}", authDto.getMatricule());
            return CourriersUtils.getResponseEntity("Le compte est expiré. Veuillez contacter l'administrateur.",
                    HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            // Gestion de toutes les autres exceptions
            log.error("Erreur lors de l'authentification", e);
            return CourriersUtils.getResponseEntity("Une erreur s'est produite lors de la connexion",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Rafraîchit le token d'accès JWT pour un utilisateur authentifié en utilisant
     * un jeton de rafraîchissement valide.
     * Cette méthode vérifie la validité du jeton de rafraîchissement et
     * l'authenticité de l'utilisateur avant de générer
     * et de retourner un nouveau jeton d'accès.
     *
     * @param refreshTokenDto Un objet de type RefreshTokenDto contenant le jeton de
     *                        rafraîchissement à valider.
     * @return ResponseEntity<?> :
     *         - 200 OK : Si le jeton de rafraîchissement est valide et un nouveau
     *         jeton d'accès est généré avec succès.
     *         - 400 BAD_REQUEST : Si le jeton de rafraîchissement est nul, vide ou
     *         mal formé (ex. MalformedJwtException).
     *         - 404 NOT_FOUND : Si l'utilisateur correspondant au jeton de
     *         rafraîchissement n'est pas trouvé dans la base de données.
     *         - 401 UNAUTHORIZED : Si le jeton de rafraîchissement est invalide.
     *         - 500 INTERNAL_SERVER_ERROR : Si une erreur inattendue survient
     *         durant le traitement.
     * @throws MalformedJwtException Si le jeton JWT est mal formé.
     * @throws Exception             Pour toute autre erreur inattendue.
     */
    @Override
    public ResponseEntity<?> refreshToken(RefreshTokenDto refreshTokenDto) {
        try {
            // Vérifier si le token de rafraîchissement est nul ou vide
            if (refreshTokenDto.getRefreshToken() == null || refreshTokenDto.getRefreshToken().isEmpty()) {
                log.error("Le token de rafraîchissement est vide ou nul.");
                return CourriersUtils.getResponseEntity("Le token de rafraîchissement est invalide.",
                        HttpStatus.BAD_REQUEST);
            }

            String username;
            try {
                // Tenter d'extraire le nom d'utilisateur du token JWT
                username = jwtService.extractUsername(refreshTokenDto.getRefreshToken());
            } catch (ExpiredJwtException e) {
                // Gérer le cas où le token JWT a expiré
                log.error("Le token rafraîchissement JWT a expiré : {}", e.getMessage());
                return CourriersUtils.getResponseEntity(
                        "Le token rafraîchissement JWT a expiré. Veuillez vous reconnecter.",
                        HttpStatus.UNAUTHORIZED);
            } catch (MalformedJwtException e) {
                // Gérer le cas où le token JWT est mal formé
                log.error("Le token JWT est mal formé : {}", e.getMessage());
                return CourriersUtils.getResponseEntity("Le token JWT est invalide. Veuillez vous reconnecter.",
                        HttpStatus.BAD_REQUEST);
            }

            // Rechercher l'utilisateur existant par son matricule
            Optional<Utilisateurs> existeUserOpt = userRepository.findByMatricule(username);
            if (!existeUserOpt.isPresent()) {
                log.error("Utilisateur non trouvé pour le matricule : {}", username);
                return CourriersUtils.getResponseEntity("Utilisateur non trouvé.", HttpStatus.NOT_FOUND);
            }

            final UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);

            // Vérifier si le token est valide
            if (!jwtService.isTokenValid(refreshTokenDto.getRefreshToken(), userDetails)) {
                log.error("Le token n'est pas valide pour l'utilisateur : {}", username);
                return CourriersUtils.getResponseEntity("Le token n'est pas valide.", HttpStatus.UNAUTHORIZED);
            }

            // Générer un nouveau token d'accès
            var jwt = jwtService.generateAccessToken(userDetails);

            JwtAuthResponseDto jwtAuthResponseDto = new JwtAuthResponseDto();
            jwtAuthResponseDto.setAccessToken(jwt);
            jwtAuthResponseDto.setRefreshToken(refreshTokenDto.getRefreshToken());

            return new ResponseEntity<JwtAuthResponseDto>(jwtAuthResponseDto, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Erreur inattendue lors de la tentative de rafraîchissement du token", e);
            return CourriersUtils.getResponseEntity("Une erreur interne s'est produite.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Réinitialise le mot de passe d'un utilisateur.
     *
     * @param resetPasswordDto Objet contenant l'ID de l'utilisateur, l'ancien mot
     *                         de passe et le nouveau mot de passe.
     * @return ResponseEntity contenant un message de succès si le mot de passe est
     *         réinitialisé avec succès,
     *         ou un message d'erreur avec un statut HTTP approprié en cas d'échec.
     */
    @Override
    public ResponseEntity<String> resetPassword(ResetPasswordDto resetPasswordDto) {
        try {
            // Rechercher l'utilisateur par son ID
            Optional<Utilisateurs> existeUserOpt = userRepository.findById(resetPasswordDto.getUserId());
            if (existeUserOpt.isEmpty()) {
                // Log un avertissement si l'utilisateur n'est pas trouvé
                log.warn("Aucun utilisateur n'a été trouvé avec l'ID " + resetPasswordDto.getUserId());
                return CourriersUtils.getResponseEntity(
                        "Aucun utilisateur n'a été trouvé avec l'ID " + resetPasswordDto.getUserId(),
                        HttpStatus.NOT_FOUND);
            }

            Utilisateurs users = existeUserOpt.get();

            // Vérifier si l'ancien mot de passe fourni correspond à celui de l'utilisateur
            if (!passwordEncoder.matches(resetPasswordDto.getOldPassword(), users.getPassword())) {
                return CourriersUtils.getResponseEntity("Votre ancien mot de passe n'est pas correct !",
                        HttpStatus.UNAUTHORIZED);
            }

            // Encoder et mettre à jour le nouveau mot de passe
            users.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
            userRepository.save(users);

            // Retourner une réponse avec un statut OK pour indiquer le succès
            return CourriersUtils.getResponseEntity(
                    "Le mot de passe a été réinitialisé avec succès.",
                    HttpStatus.OK);
        } catch (Exception e) {
            // Log l'erreur et retourner une réponse avec un statut d'erreur interne
            log.error("Erreur lors de la réinitialisation du mot de passe", e.getMessage());
            return CourriersUtils.getResponseEntity("Une erreur interne s'est produite.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
