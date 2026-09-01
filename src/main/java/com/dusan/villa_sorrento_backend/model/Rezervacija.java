/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
/**
 * @author Dusan
 * Predstavlja rezervaciju koju kreira korisnik sistema.
 * Rezervacija objedinjuje jednu ili vise stavki rezervacije, ukupni iznos,
 * datum kreiranja i placanja.
 */
@Entity 
@Data 
@NoArgsConstructor 
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"user", "placanja", "stavkeRezervacije"})
public class Rezervacija {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idRezervacija;
    
    @Column(nullable = false)
    private double iznos;
    
    @Column(nullable = false)
    private LocalDate datumKreiranja;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "user_id", nullable=false)
    @ToString.Exclude
    private User user;
    
    @OneToMany(mappedBy = "rezervacija", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Placanje> placanja = new HashSet<>();
    
    @OneToMany(mappedBy = "rezervacija", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<StavkaRezervacije> stavkeRezervacije;
}
