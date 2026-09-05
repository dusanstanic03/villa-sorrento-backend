/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.model;

import jakarta.persistence.*;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
/**
 * Predstavlja korisnika sistema vile Sorrento.
 * Korisnik moze imati ulogu klijenta ili administratora. Klijent kreira i
 * pregleda svoje rezervacije, dok administrator upravlja sobama, klijentima i
 * rezervacijama.
 *
 * @author Dusan
 */
@Entity //markiram klasu kao JPA entity
@Data //uz pomoc lomboka smanjujem boiler plate kod i kreiram getere, setere, equals, toString i hashCode automatski
@NoArgsConstructor // Program sam kreira besparametarski konstruktor
@AllArgsConstructor // Program sam kreira parametarski konstruktor gde su parametri sva polja klase
@Table(name = "app_user") // Specificiram ime tabele da ne bi doslo do konflikta sa rezervisanom reci user
@EqualsAndHashCode(callSuper = false, exclude = {"rezervacije"})
public class User {

    /**
     * Jedinstveni identifikator korisnika.
     * Vrednost se automatski generise u bazi podataka.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment opcija za idUser
    private long idUser;

    /**
     * Korisnicko ime korisnika koje se koristi za prijavu u sistem.
     * Ne sme biti null, prazno niti krace od 3 ili duze od 15 karaktera.
     * Mora biti jedinstveno u bazi podataka.
     */
    @NotBlank(message = "Korisnicko ime je obavezno.")
    @Size(min = 3, max = 15, message = "Korisnicko ime mora imati izmedju 3 i 15 karaktera.")
    @Column(unique=true, nullable = false)
    private String username;

    /**
     * Lozinka korisnika.
     * Ne sme biti null, prazna niti kraca od 4 karaktera.
     */
    @NotBlank(message = "Lozinka je obavezna.")
    @Size(min = 4, message = "Lozinka mora imati najmanje 4 karaktera.")
    @Column(nullable = false)
    private String password;

    /**
     * Uloga korisnika u sistemu.
     * Ne sme biti null ni prazna. Dozvoljene vrednosti su admin i klijent.
     */
    @NotBlank(message = "Uloga je obavezna.")
    @Pattern(regexp = "admin|klijent", message = "Uloga mora biti admin ili klijent.")
    @Column(nullable = false)
    private String uloga;

    /**
     * Broj isprave korisnika.
     * Ne sme biti null, prazan, kraci od 5 ili duzi od 20 karaktera.
     * Sme sadrzati samo cifre.
     */
    @NotBlank(message = "Broj isprave je obavezan.")
    @Size(min = 5, max = 20, message = "Broj isprave mora imati izmedju 5 i 20 cifara")
    @Pattern(regexp = "\\d+", message = "Broj isprave sme sadrzati samo cifre.")
    @Column(nullable = false)
    private String brojIsprave;

    /**
     * Broj telefona korisnika.
     * Ne sme biti null, prazan, kraci od 5 ili duzi od 20 karaktera.
     * Sme sadrzati samo cifre.
     */
    @NotBlank(message = "Broj telefona je obavezan.")
    @Size(min = 5, max = 20, message = "Broj telefona mora imati izmedju 5 i 20 cifara")
    @Pattern(regexp = "\\d+", message = "Broj telefona sme sadrzati samo cifre.")
    @Column(nullable = false)
    private String brojTelefona;
    
    // Koristimo one-to-many relaciju prema Rezervaciji sto znaci da jedan user moze da napravi vise rezervacija
    //mapppedBy treba da pokaze polje u Rezervaciji koje je vlasnik relacije
    //CascadeType.ALL - ako napravimo neku pormenu na User-u npr. ta pormena ce se kaskadno prosiriti i na povezane objekte Rezervacije
    //orphanRemoval nam omogucava da ako npr. uklonimo objekat Rezervacije iz seta on ce biti obrisan i iz baze
    /**
     * Rezervacije koje je korisnik kreirao.
     * Jedan korisnik moze imati vise rezervacija.
     */
    @OneToMany(mappedBy="user",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Rezervacija> rezervacije; //pravimo skup Rezervacija ovde iako u Rezervaciji ima psoljni kljuc prema Useru da bismo omogucili bidirekcionu vezu
}
