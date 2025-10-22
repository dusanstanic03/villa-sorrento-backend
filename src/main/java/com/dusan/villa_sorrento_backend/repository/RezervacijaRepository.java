/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.dusan.villa_sorrento_backend.repository;

import com.dusan.villa_sorrento_backend.model.Rezervacija;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dusan
 */
@Repository
public interface RezervacijaRepository extends JpaRepository<Rezervacija, Long> {
    List<Rezervacija> findByUserIdUser(Long userId); // Za pretragu rezervacija po korisniku
    
}
