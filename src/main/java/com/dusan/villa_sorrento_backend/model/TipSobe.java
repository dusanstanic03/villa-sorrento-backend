package com.dusan.villa_sorrento_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Dusan
 * Predstavlja kategoriju sobe u vili Sorrento.
 * Tip sobe definise naziv, opis i kapacitet koji se mogu koristiti za vise
 * konkretnih soba.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"sobe"})
public class TipSobe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipSobe;

    @Column(nullable = false, unique = true)
    private String naziv;

    @Column(nullable = false)
    private String opis;

    @Column(nullable = false)
    private Integer kapacitet;

    @OneToMany(mappedBy = "tipSobe", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Soba> sobe = new HashSet<>();
}
