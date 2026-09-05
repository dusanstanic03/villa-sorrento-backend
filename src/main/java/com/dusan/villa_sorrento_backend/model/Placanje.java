/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Predstavlja placanje povezano sa rezervacijom.
 * Placanje cuva nacin placanja, status, iznos i datum placanja. Jedna
 * rezervacija moze imati vise evidentiranih placanja.
 *
 * @author Dusan
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"rezervacija"}) 

public class Placanje {

    /**
     * Jedinstveni identifikator placanja.
     * Vrednost se automatski generise u bazi podataka.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlacanje;

    /**
     * Status placanja.
     * Ne sme biti null ni prazan. Dozvoljene vrednosti su PENDING, COMPLETED,
     * PROCESSED i FAILED.
     */
    @NotBlank(message = "Status placanja je obavezan.")
    @Pattern(regexp = "PENDING|COMPLETED|PROCESSED|FAILED", message = "Status placanja nije dozvoljen.")
    @Column(nullable = false)
    private String status;

    /**
     * Nacin placanja.
     * Ne sme biti null ni prazan. Dozvoljene vrednosti su CARD, CRYPTO i CASH.
     */
    @NotBlank(message = "Nacin placanja je obavezan.")
    @Pattern(regexp = "CARD|CRYPTO|CASH", message = "Nacin placanja mora biti CARD, CRYPTO ili CASH.")
    @Column(nullable = false)
    private String nacinPlacanja;

    /**
     * Iznos placanja.
     * Ne sme biti null i mora biti pozitivna vrednost.
     */
    @NotNull(message = "Iznos placanja je obavezan.")
    @Positive(message = "Iznos placanja mora biti pozitivan.")
    @Column(nullable = false)
    private Double iznos;

    /**
     * Datum evidentiranja placanja.
     * Ne sme biti null.
     */
    @NotNull(message = "Datum placanja je obavezan.")
    @Column(nullable = false)
    private LocalDate datumPlacanja;

    /**
     * Rezervacija na koju se placanje odnosi.
     * Ne sme biti null.
     */
    @NotNull(message = "Rezervacija je obavezna.")
    @ManyToOne(fetch = FetchType.LAZY) // Vise placanja se moze odnositi na jednu rezervaciju
    @JoinColumn(name = "rezervacija_id", nullable = false) //spoljni kljuc
    @ToString.Exclude
    private Rezervacija rezervacija;
}
