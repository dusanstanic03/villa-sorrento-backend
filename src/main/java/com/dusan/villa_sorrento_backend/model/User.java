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
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Entity //markiram klasu kao JPA entity
@Data //uz pomoc lomboka smanjujem boiler plate kod i kreiram getere, setere, equals, toString i hashCode automatski
@NoArgsConstructor // Program sam kreira besparametarske konstruktore
@AllArgsConstructor // Program sam kreira parametarske konstruktore gde su parametri sva polja klase
@Table(name = "app_user") // Specificiram ime tabele da ne bi doslo do konflikta sa rezervisanom reci user
@EqualsAndHashCode(callSuper = false, exclude = {"rezervacije"})


public class User {
    @Id // Oznacavam idUSer lap primarni kljuc
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment opcija za idUser
    private long idUser;
    
    @Column(unique=true, nullable = false) // Username bi trebalo biti jedinstven i ne null vrednost
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String uloga;
    
    @Column(nullable = false)
    private String brojIsprave;
    
    @Column(nullable = false)
    private String brojTelefona;
    
    // Koristimo one-to-many relaciju prema Rezervaciji sto znaci da jedan user moze da napravi vise rezervacija
    //mapppedBy treba da pokaze polje u Rezervaciji koje je vlasnik relacije
    //CascadeType.ALL - ako napravimo neku pormenu na User-u npr. ta pormena ce se kaskadno prosiriti i na povezane objekte Rezervacije
    //orphanRemoval nam omogucava da ako npr. uklonimo objekat Rezervacije iz seta on ce biti obrisan i iz baze
    @OneToMany(mappedBy="user",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Rezervacija> rezervacije; //pravimo skup Rezervacija ovde iako u Rezervaciji ima psoljni kljuc prema Useru da bismo omogucili bidirekcionu vezu
}
