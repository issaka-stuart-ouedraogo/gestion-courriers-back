package com.cil.bf.gestion_courriers_back.models;

import com.cil.bf.gestion_courriers_back.utils.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "utilisateurs")
public class Utilisateurs extends AbstractAuditingEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Column(length = 50, unique = true)
    private String matricule;

    @Size(max = 50)
    @Column(name = "nom", length = 50)
    private String nom;

    @Size(max = 150)
    @Column(name = "prenom", length = 150)
    private String prenom;

    @Column(name = "contact", length = 20, unique = true)
    private String contact;

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(name = "login", length = 50, unique = true, nullable = false)
    private String login;

    @Size(min = 2, max = 60)
    @Column(length = 60)
    private String password;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    /**
     * true : Le compte n'a pas expiré et est encore valide.
     * false : Le compte a expiré et l'utilisateur ne peut pas se connecter.
     */
    private boolean accountNonExpired;

    /**
     * true : Le compte est non verrouillé et accessible.
     * false : Le compte est verrouillé, et l'utilisateur ne peut pas se connecter.
     */
    private boolean accountNonLocked;

    /**
     * true : true : Les informations d'identification sont encore valides.
     * false : Les informations d'identification ont expiré, et l'utilisateur doit
     * les mettre à jour avant de pouvoir se connecter.
     */
    private boolean credentialsNonExpired;

    /**
     * true : Le compte est activé et l'utilisateur peut se connecter.
     * false : Le compte est désactivé, et l'utilisateur ne peut pas se connecter.
     */
    @NotNull
    @Column(nullable = false)
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
    @JoinTable(name = "utilisateurs_profils", joinColumns = @JoinColumn(name = "utilisateur_id"), inverseJoinColumns = @JoinColumn(name = "profil_id"))
    private Set<Profils> profilList = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        Set<Privileges> privilegeList = new HashSet<>();

        // Ajoute tous les privilèges de chaque profil de l'utilisateur
        this.profilList.forEach(profil -> privilegeList.addAll(profil.getPrivilegeList()));

        // Convertit chaque privilège en SimpleGrantedAuthority et l'ajoute à
        // authorities
        authorities = privilegeList.stream()
                .map(privilege -> new SimpleGrantedAuthority(privilege.getCode()))
                .collect(Collectors.toSet());

        return authorities;
    }

    @Override
    public String getUsername() {
        return matricule;
    }
}
