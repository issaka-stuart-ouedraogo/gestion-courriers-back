package com.cil.bf.gestion_courriers_back.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "profils")
public class Profils extends AbstractAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "libelle", unique = true)
    private String libelle;

    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
    @JoinTable(name = "profils_privileges", joinColumns = @JoinColumn(name = "profil_id"), inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    private Set<Privileges> privilegeList = new HashSet<>();

}
