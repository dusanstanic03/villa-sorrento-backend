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
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Predstavlja gosta koji boravi u sobi u okviru stavke rezervacije.
 * Gost ne mora biti klijent, tj. osoba koja pravi rezervaciju ali moze.
 * Isti gost moze biti povezan sa vise stavki rezervacije.
 *
 * @author Dusan
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"stavkeRezervacije"})
public class Gost {

    /**
     * Jedinstveni identifikator gosta.
     * Vrednost se automatski generise u bazi podataka.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGost;

    /**
     * Ime gosta.
     * Ne sme biti null, prazno, krace od 3 ili duze od 15 karaktera.
     */
    @NotBlank(message = "Ime gosta je obavezno.")
    @Size(min = 3, max = 15, message = "Ime mora imati izmedju 3 i 15 slova.")
    @Column(nullable = false)
    private String ime;

    /**
     * Prezime gosta.
     * Ne sme biti null, prazno, krace od 3 ili duze od 15 karaktera.
     */
    @NotBlank(message = "Prezime gosta je obavezno.")
    @Size(min = 3, max = 15, message = "Prezime mora imati izmedju 3 i 15 slova.")
    @Column(nullable = false)
    private String prezime;

    /**
     * Broj isprave gosta.
     * Ne sme biti null, prazan, kraci od 5 ili duzi od 20 karaktera.
     * Sme sadrzati samo cifre.
     */
    @NotBlank(message = "Broj isprave gosta je obavezan.")
    @Pattern(regexp = "\\d+", message = "Broj isprave sme sadrzati samo cifre.")
    @Size(min = 5, max = 20, message = "Broj isprave mora imati izmedju 5 i 20 cifara.")
    @Column(nullable = false, unique = true)
    private String brojIsprave;

    /**
     * Broj telefona gosta.
     * Ne sme biti null, prazan, kraci od 5 ili duzi od 20 karaktera.
     * Sme sadrzati samo cifre.
     */
    @NotBlank(message = "Broj telefona gosta je obavezan.")
    @Pattern(regexp = "\\d+", message = "Broj telefona sme sadrzati samo cifre.")
    @Size(min = 5, max = 20, message = "Broj telefona mora imati izmedju 5 i 20 cifara.")
    @Column(nullable = false)
    private String brojTelefona;

    /**
     * Stavke rezervacije u kojima je gost evidentiran.
     * Jedan gost moze biti povezan sa vise stavki rezervacije.
     */
    @ManyToMany(mappedBy = "gosti", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<StavkaRezervacije> stavkeRezervacije = new HashSet<>();
}
