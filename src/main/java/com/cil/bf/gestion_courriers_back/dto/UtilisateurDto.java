package com.cil.bf.gestion_courriers_back.dto;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import com.cil.bf.gestion_courriers_back.models.Privileges;
import com.cil.bf.gestion_courriers_back.models.Utilisateurs;
import com.cil.bf.gestion_courriers_back.utils.Constants;
import com.cil.bf.gestion_courriers_back.utils.CourriersUtils;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UtilisateurDto {

    private Long id;

    private String nom;

    private String login;

    private String email;

    private String prenom;

    private String contact;

    private boolean enabled;

    private String password;

    private String matricule;

    private String lastModifiedBy;

    private boolean accountNonLocked;

    // private Instant lastModifiedDate;

    private Set<ProfilDto> profilDtoList;

    private Set<PrivilegeDto> privilegeDtoList;

    @SuppressWarnings("null")
    public static UtilisateurDto fromEntity(Utilisateurs utilisateur) {

        if (utilisateur == null) {
            CourriersUtils.getResponseEntity(Constants.DONNEES_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        Set<Privileges> privilegeList = new HashSet<>();
        utilisateur.getProfilList().stream().forEach(priv -> {
            privilegeList.addAll(priv.getPrivilegeList());
        });
        UtilisateurDto utilisateurDto = UtilisateurDto.builder()
                .id(utilisateur.getId())
                .enabled(utilisateur.isEnabled())
                .login(utilisateur.getLogin())
                .nom(utilisateur.getNom())
                .email(utilisateur.getEmail())
                .prenom(utilisateur.getPrenom())
                .contact(utilisateur.getContact())
                .matricule(utilisateur.getMatricule())
                .profilDtoList(utilisateur.getProfilList() != null
                        ? utilisateur.getProfilList().stream().map(ProfilDto::fromEntity).collect(Collectors.toSet())
                        : null)
                .privilegeDtoList(privilegeList != null
                        ? privilegeList.stream().map(PrivilegeDto::fromEntity).collect(Collectors.toSet())
                        : null)
                .build();
        return utilisateurDto;
    }

    @SuppressWarnings("null")
    public static Utilisateurs toEntity(UtilisateurDto utilisateurDto) {
        if (utilisateurDto == null) {
            CourriersUtils.getResponseEntity(Constants.DONNEES_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        Utilisateurs utilisateur = new Utilisateurs();

        utilisateur.setId(utilisateurDto.getId());
        utilisateur.setNom(utilisateurDto.getNom());
        utilisateur.setLogin(utilisateurDto.getLogin());
        utilisateur.setEmail(utilisateurDto.getEmail());
        utilisateur.setPrenom(utilisateurDto.getPrenom());
        utilisateur.setContact(utilisateurDto.getContact());
        utilisateur.setMatricule(utilisateurDto.getMatricule());
        // utilisateur.setLastModifiedBy(utilisateurDto.getLastModifiedBy());
        // utilisateur.setLastModifiedDate(utilisateurDto.getLastModifiedDate());

        // utilisateur.setEnabled(utilisateurDto.isEnabled());
        // utilisateur.setPassword(utilisateurDto.getPassword());
        // utilisateur.setAccountNonLocked(utilisateurDto.isAccountNonLocked());
        // utilisateur.setDeleted(utilisateurDto.isDeleted());
        // utilisateur.setActivationKey(utilisateurDto.getActivationKey());
        // utilisateur.setCreatedBy(utilisateurDto.getActivationKey());
        // utilisateur.setCreatedDate(utilisateurDto.getCreatedDate());

        // utilisateur.setConfirmationExpireDate(utilisateurDto.confirmationExpireDate);
        return utilisateur;
    }
}
