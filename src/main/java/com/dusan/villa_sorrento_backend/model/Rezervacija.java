/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
/**
 * Predstavlja rezervaciju koju kreira klijent.
 * Rezervacija objedinjuje jednu ili vise stavki rezervacije, ukupni iznos,
 * datum kreiranja i placanja.
 *
 * @author Dusan
 */
@Entity 
@Data 
@NoArgsConstructor 
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"user", "placanja", "stavkeRezervacije"})
public class Rezervacija {

    /**
     * Jedinstveni identifikator rezervacije.
     * Vrednost se automatski generise u bazi podataka.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idRezervacija;

    /**
     * Ukupan iznos rezervacije.
     * Ne sme biti negativan.
     */
    @PositiveOrZero(message = "Iznos rezervacije ne sme biti negativan.")
    @Column(nullable = false)
    private double iznos;

    /**
     * Datum kreiranja rezervacije.
     * Ne sme biti null.
     */
    @NotNull(message = "Datum kreiranja rezervacije je obavezan.")
    @Column(nullable = false)
    private LocalDate datumKreiranja;

    /**
     * Klijent koji je kreirao rezervaciju.
     * Ne sme biti null.
     */
    @NotNull(message = "Klijent je obavezan.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "user_id", nullable=false)
    @ToString.Exclude
    private User user;

    /**
     * Placanja evidentirana za ovu rezervaciju.
     * Jedna rezervacija moze imati vise placanja.
     */
    @OneToMany(mappedBy = "rezervacija", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Placanje> placanja = new HashSet<>();

    /**
     * Stavke koje cine rezervaciju.
     * Jedna rezervacija moze imati jednu ili vise stavki rezervacije.
     */
    @OneToMany(mappedBy = "rezervacija", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<StavkaRezervacije> stavkeRezervacije;
}
