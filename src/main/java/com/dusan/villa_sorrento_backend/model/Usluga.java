package com.dusan.villa_sorrento_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Predstavlja usluge koje gost moze izabrati uz stavku rezervacije.
 * Usluge povecavaju osnovnu cenu smestaja, na primer dorucak, parking ili spa
 * paket. Samo aktivne usluge se nude klijentima pri kreiranju rezervacije.
 *
 * @author Dusan
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"stavkeRezervacije"})
public class Usluga {

    /**
     * Jedinstveni identifikator usluge.
     * Vrednost se automatski generise u bazi podataka.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsluga;

    /**
     * Naziv usluge.
     * Ne sme biti null, prazan, kraci od 2 ili duzi od 50 karaktera.
     * Mora biti jedinstven u bazi podataka.
     */
    @NotBlank(message = "Naziv usluge je obavezan.")
    @Size(min = 2, max = 50, message = "Naziv usluge mora imati izmedju 2 i 50 karaktera.")
    @Column(nullable = false, unique = true)
    private String naziv;

    /**
     * Opis usluge.
     * Ne sme biti null ni prazan. Ne sme biti duzi od 500 karaktera.
     */
    @NotBlank(message = "Opis usluge je obavezan.")
    @Size(max = 500, message = "Opis usluge ne sme imati vise od 500 karaktera.")
    @Column(nullable = false)
    private String opis;

    /**
     * Cena usluge.
     * Ne sme biti null i ne sme biti negativna.
     */
    @NotNull(message = "Cena usluge je obavezna.")
    @PositiveOrZero(message = "Cena usluge ne sme biti negativna.")
    @Column(nullable = false)
    private Double cena;

    /**
     * Status aktivnosti usluge.
     * Ne sme biti null. Samo aktivne usluge se prikazuju klijentima pri rezervaciji.
     */
    @NotNull(message = "Status aktivnosti usluge je obavezan.")
    @Column(nullable = false)
    private Boolean aktivna;

    /**
     * Stavke rezervacije na kojima je izabrana ova usluga.
     * Jedna usluga moze biti povezana sa vise stavki rezervacije.
     */
    @ManyToMany(mappedBy = "usluge", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<StavkaRezervacije> stavkeRezervacije = new HashSet<>();
}
