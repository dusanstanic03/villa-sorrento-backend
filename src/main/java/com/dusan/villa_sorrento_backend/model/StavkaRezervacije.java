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

import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"rezervacija", "soba"})

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
    
}
