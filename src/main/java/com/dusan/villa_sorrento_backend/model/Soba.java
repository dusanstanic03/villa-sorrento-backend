/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.model;

/**
 *
 * @author Dusan
 */
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    @Column(nullable = false)
    private String tipSobe;
    
    @Column(nullable = false)
    private String slikaUrl;
    
    @OneToMany(mappedBy = "soba", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<StavkaRezervacije> stavkeRezervacije;
    
    
}
