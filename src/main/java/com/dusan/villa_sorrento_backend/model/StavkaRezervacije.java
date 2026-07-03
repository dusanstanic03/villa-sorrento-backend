/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Dusan
 * Predstavlja jednu stavku rezervacije za jednu sobu i jedan period boravka.
 *
 * Stavka rezervacije povezuje rezervaciju, sobu, goste koji borave u toj sobi i
 * dodatne usluge koje su izabrane za taj boravak. Cena stavke se racuna na
 * osnovu broja nocenja, cene sobe i dodatnih usluga.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"rezervacija", "soba", "gosti", "usluge"})

public class StavkaRezervacije {  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private int rb;
    
    @Column(nullable = false)
    private LocalDate datumOd;
    
    @Column(nullable = false)
    private LocalDate datumDo;
    
    @Column(nullable = false)
    private double iznos;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rezervacija_id", nullable = false)
    @ToString.Exclude
    private Rezervacija rezervacija;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="soba_id", nullable = false)
    @ToString.Exclude
    private Soba soba;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "stavka_rezervacije_gost",
            joinColumns = @JoinColumn(name = "stavka_rezervacije_id"),
            inverseJoinColumns = @JoinColumn(name = "gost_id")
    )
    @ToString.Exclude
    private Set<Gost> gosti = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "stavka_rezervacije_usluga",
            joinColumns = @JoinColumn(name = "stavka_rezervacije_id"),
            inverseJoinColumns = @JoinColumn(name = "usluga_id")
    )
    @ToString.Exclude
    private Set<Usluga> usluge = new HashSet<>();
    
}
