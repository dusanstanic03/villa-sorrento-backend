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
 * Predstavlja gosta koji boravi u sobi u okviru stavke rezervacije.
 *
 * Gost nije isto sto i klijent. Klijent pravi rezervaciju, a gosti su
 * osobe koje borave u rezervisanim sobama. Isti gost moze biti povezan
 * sa vise stavki rezervacije.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"stavkeRezervacije"})
public class Gost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGost;

    @Column(nullable = false)
    private String ime;

    @Column(nullable = false)
    private String prezime;

    @Column(nullable = false, unique = true)
    private String brojIsprave;

    @Column(nullable = false)
    private String brojTelefona;

    @ManyToMany(mappedBy = "gosti", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<StavkaRezervacije> stavkeRezervacije = new HashSet<>();
}
