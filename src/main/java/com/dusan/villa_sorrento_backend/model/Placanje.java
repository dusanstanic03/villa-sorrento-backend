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
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"rezervacija"}) 

public class Placanje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPlacanje;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String nacinPlacanja;

    @Column(nullable = false)
    private Double iznos;

    @Column(nullable = false)
    private LocalDate datumPlacanja;
    
    @ManyToOne(fetch = FetchType.LAZY) // Vise placanja se moze odnositi na jednu rezervaciju
    @JoinColumn(name = "rezervacija_id", nullable = false) //spoljni kljuc
    @ToString.Exclude
    private Rezervacija rezervacija;
    
    

}
