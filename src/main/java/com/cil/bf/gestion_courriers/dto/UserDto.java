package com.cil.bf.gestion_courriers.dto;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;

import com.cil.bf.gestion_courriers.models.User;
import com.cil.bf.gestion_courriers.utils.Constants;
import com.cil.bf.gestion_courriers.utils.CourriersUtils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private Long id;

    private String matricule;

    private String nom;

    private String prenom;

    private String contact;

    private boolean actif = false;

    private String login;

    private String password;

    private String email;

    private boolean deleted = false;

    private Set<ProfilDto> profilDtoList;

    private Set<PrivilegeDto> privilegeDtoList;

    public static UserDto fromEntity(User user) {

        if (user == null) {
            CourriersUtils.getResponseEntity(Constants.DONNEES_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .actif(user.isActif())
                .login(user.getLogin())
                .nom(user.getNom())
                .email(user.getEmail())
                .prenom(user.getPrenom())
                .contact(user.getContact())
                .matricule(user.getMatricule())
                .profilDtoList(user.getProfilList() != null
                        ? user.getProfilList().stream().map(ProfilDto::fromEntity).collect(Collectors.toSet())
                        : null)

                .build();

        return userDto;
    }

    public static User toEntity(UserDto userDto) {
        if (userDto == null) {
            CourriersUtils.getResponseEntity(Constants.DONNEES_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        User user = new User();

        user.setId(userDto.getId());
        user.setNom(userDto.getNom());
        user.setActif(userDto.isActif());
        user.setLogin(userDto.getLogin());
        user.setEmail(userDto.getEmail());
        user.setContact(userDto.getContact());
        user.setPrenom(userDto.getPrenom());
        user.setPassword(userDto.getPassword());
        user.setMatricule(userDto.getMatricule());

        // user.setDeleted(userDto.isDeleted());
        // user.setActivationKey(userDto.getActivationKey());
        // user.setCreatedBy(userDto.getActivationKey());
        // user.setCreatedDate(userDto.getCreatedDate());
        // user.setLastModifiedBy(userDto.getLastModifiedBy());
        // user.setLastModifiedDate(userDto.getLastModifiedDate());
        // user.setConfirmationExpireDate(userDto.confirmationExpireDate);
        return user;
    }
}
