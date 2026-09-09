/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Predstavlja konkretnu sobu koja se moze rezervisati u vili Sorrento.
 * Soba sadrzi cenu, opis, dostupnost, urlSlike, jedinicu mere, a njen tip se vodi kao posebna
 * domenska klasa {@link TipSobe}. Jedna soba moze biti obuhvacena kroz vise
 * stavki rezervacije u razlicitim vremenskim periodima.
 *
 * @author Dusan
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"stavkeRezervacije"})

public class Soba {

    /**
     * Jedinstveni identifikator sobe.
     * Vrednost se automatski generise u bazi podataka.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSoba;

    /**
     * Opis sobe.
     * Ne sme biti null, prazan, kraci od 5 ili duzi od 255 karaktera.
     */
    @NotBlank(message = "Opis sobe je obavezan.")
    @Size(min = 5, max = 255, message = "Opis sobe mora imati izmedju 5 i 255 karaktera.")
    @Column(nullable = false) 
    private String opis;

    /**
     * Cena sobe po jedinici mere.
     * Ne sme biti null i mora biti pozitivna vrednost.
     */
    @NotNull(message = "Cena sobe je obavezna.")
    @Positive(message = "Cena sobe mora biti pozitivna vrednost.")
    @Column(nullable = false) 
    private Double cena;

    /**
     * Jedinica mere za cenu sobe.
     * Ne sme biti null ni prazna. Dozvoljena vrednost je noc.
     */
    @NotBlank(message = "Jedinica mere je obavezna.")
    @Pattern(regexp = "noc", message = "Jedinica mere mora biti noc.")
    @Column(nullable = false)
    private String jedinicaMere;

    /**
     * Informacija da li je soba dostupna za rezervisanje (npr. zbog renoviranja moze biti nedostupna).
     * Ne sme biti null.
     */
    @NotNull(message = "Dostupnost sobe je obavezna.")
    @Column(nullable = false)
    private Boolean dostupna;

    /**
     * Putanja ili URL slike sobe.
     * Ne sme biti null ni prazna.
     */
    @NotBlank(message = "URL slike je obavezan.")
    @Column(nullable = false)
    private String slikaUrl;

    /**
     * Tip kojem soba pripada.
     * Ne sme biti null.
     */
    @NotNull(message = "Tip sobe je obavezan.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tip_sobe_id")
    @ToString.Exclude
    private TipSobe tipSobe;

    /**
     * Stavke rezervacije koje se odnose na ovu sobu.
     * Jedna soba moze se pojaviti u vise stavki rezervacije.
     */
    @OneToMany(mappedBy = "soba", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<StavkaRezervacije> stavkeRezervacije;
    
    
}
