/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Predstavlja jednu stavku rezervacije za jednu sobu i jedan period boravka.
 * Stavka rezervacije povezuje rezervaciju, sobu, goste koji borave u toj sobi i
 * usluge koje su izabrane za taj boravak. Cena stavke se racuna na
 * osnovu broja nocenja, cene sobe i dodatnih usluga.
 *
 * @author Dusan
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"rezervacija", "soba", "gosti", "usluge"})

public class StavkaRezervacije {

    /**
     * Jedinstveni identifikator stavke rezervacije.
     * Vrednost se automatski generise u bazi podataka.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Redni broj stavke u okviru rezervacije.
     * Mora biti najmanje 1.
     */
    @Min(value = 1, message = "Redni broj stavke mora biti najmanje 1.")
    @Column(nullable = false)
    private int rb;

    /**
     * Datum pocetka boravka.
     * Ne sme biti null.
     */
    @NotNull(message = "Datum pocetka boravka je obavezan.")
    @Column(nullable = false)
    private LocalDate datumOd;

    /**
     * Datum kraja boravka.
     * Ne sme biti null.
     */
    @NotNull(message = "Datum kraja boravka je obavezan.")
    @Column(nullable = false)
    private LocalDate datumDo;

    /**
     * Iznos stavke rezervacije.
     * Ne sme biti negativan.
     */
    @PositiveOrZero(message = "Iznos stavke ne sme biti negativan.")
    @Column(nullable = false)
    private double iznos;

    /**
     * Rezervacija kojoj stavka pripada.
     * Ne sme biti null.
     */
    @NotNull(message = "Rezervacija je obavezna.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rezervacija_id", nullable = false)
    @ToString.Exclude
    private Rezervacija rezervacija;

    /**
     * Soba koja se rezervise ovom stavkom.
     * Ne sme biti null.
     */
    @NotNull(message = "Soba je obavezna.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="soba_id", nullable = false)
    @ToString.Exclude
    private Soba soba;

    /**
     * Gosti koji borave u sobi iz ove stavke rezervacije.
     * Stavka rezervacije moze imati vise gostiju.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "stavka_rezervacije_gost",
            joinColumns = @JoinColumn(name = "stavka_rezervacije_id"),
            inverseJoinColumns = @JoinColumn(name = "gost_id")
    )
    @ToString.Exclude
    private Set<Gost> gosti = new HashSet<>();

    /**
     * Usluge izabrane za ovu stavku rezervacije.
     * Stavka rezervacije moze imati vise usluga.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "stavka_rezervacije_usluga",
            joinColumns = @JoinColumn(name = "stavka_rezervacije_id"),
            inverseJoinColumns = @JoinColumn(name = "usluga_id")
    )
    @ToString.Exclude
    private Set<Usluga> usluge = new HashSet<>();
    
}
