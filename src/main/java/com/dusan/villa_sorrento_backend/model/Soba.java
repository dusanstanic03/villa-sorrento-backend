/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Predstavlja konkretnu sobu koja se moze rezervisati u vili Sorrento.
 *
 * Soba sadrzi cenu, opis, dostupnost i sliku, a njen tip se vodi kao posebna
 * domenska klasa {@link TipSobe}. Jedna soba moze biti obuhvacena kroz vise
 * stavki rezervacije u razlicitim vremenskim periodima.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"stavkeRezervacije"})

public class Soba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSoba;

    @Column(nullable = false) 
    private String opis;

    @Column(nullable = false) 
    private Double cena;

    
    @Column(nullable = false)
    private String jedinicaMere;

    @Column(nullable = false)
    private Boolean dostupna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tip_sobe_id")
    @ToString.Exclude
    private TipSobe tipSobe;
    
    @Column(nullable = false)
    private String slikaUrl;
    
    @OneToMany(mappedBy = "soba", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<StavkaRezervacije> stavkeRezervacije;
    
    
}
