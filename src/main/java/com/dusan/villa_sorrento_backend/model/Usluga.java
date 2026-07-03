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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Predstavlja uslugu koju gost moze izabrati uz stavku rezervacije.
 *
 * Usluge prosiruju osnovnu cenu smestaja, na primer dorucak, parking ili spa
 * paket. Aktivne usluge se nude klijentima pri kreiranju rezervacije.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"stavkeRezervacije"})
public class Usluga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsluga;

    @Column(nullable = false, unique = true)
    private String naziv;

    @Column(nullable = false)
    private String opis;

    @Column(nullable = false)
    private Double cena;

    @Column(nullable = false)
    private Boolean aktivna;

    @ManyToMany(mappedBy = "usluge", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<StavkaRezervacije> stavkeRezervacije = new HashSet<>();
}
