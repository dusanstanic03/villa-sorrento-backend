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

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Predstavlja kategoriju sobe u vili Sorrento.
 * Tip sobe definise naziv, opis i kapacitet koji se mogu koristiti za vise
 * konkretnih soba.
 *
 * @author Dusan
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"sobe"})
public class TipSobe {

    /**
     * Jedinstveni identifikator tipa sobe.
     * Vrednost se automatski generise u bazi podataka.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipSobe;

    /**
     * Naziv tipa sobe.
     * Ne sme biti null, prazan, kraci od 3 ili duzi od 50 karaktera.
     * Mora biti jedinstven u bazi podataka.
     */
    @NotBlank(message = "Naziv tipa sobe je obavezan.")
    @Size(min = 3, max = 50, message = "Naziv tipa sobe mora imati izmedju 3 i 50 slova.")
    @Column(nullable = false, unique = true)
    private String naziv;

    /**
     * Opis tipa sobe.
     * Ne sme biti null ni prazan. Ne sme biti duzi od 500 karaktera.
     */
    @NotBlank(message = "Opis tipa sobe je obavezan.")
    @Size(max = 500, message = "Opis tipa sobe ne sme imati vise od 500 karaktera.")
    @Column(nullable = false)
    private String opis;

    /**
     * Kapacitet tipa sobe, odnosno maksimalan broj gostiju.
     * Ne sme biti null. Mora biti najmanje 1 i najvise 4.
     */
    @NotNull(message = "Kapacitet je obavezan.")
    @Min(value = 1, message = "Kapacitet mora biti najmanje 1.")
    @Max(value = 4, message = "Kapacitet ne sme biti veci od 4.")
    @Column(nullable = false)
    private Integer kapacitet;

    /**
     * Sobe koje pripadaju ovom tipu sobe.
     * Jedan tip sobe moze biti povezan sa vise konkretnih soba.
     */
    @OneToMany(mappedBy = "tipSobe", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Soba> sobe = new HashSet<>();
}
