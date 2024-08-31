package com.cil.bf.gestion_courriers.models;

import com.cil.bf.gestion_courriers.utils.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@SuppressWarnings("deprecation")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class User extends AbstractAuditingEntity {

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

    @Column(nullable = false)
    private boolean actif = false;

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

    private boolean deleted = false;

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
    @JoinTable(name = "users_profils", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "profil_id"))
    private Set<Profil> profilList = new HashSet<>();
}
