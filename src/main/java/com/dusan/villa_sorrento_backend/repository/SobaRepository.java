/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.dusan.villa_sorrento_backend.repository;

import com.dusan.villa_sorrento_backend.model.Soba;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Dusan
 */
public interface SobaRepository extends JpaRepository<Soba, Long> {
    List<Soba> findByDostupna(Boolean dostupna);
    List<Soba> findByTipSobe(String tipSobe);
    List<Soba> findByCenaBetween(Double minCena, Double maxCena);
  
    List<Soba> findByDostupnaAndTipSobeAndCenaBetween(Boolean dostupna, String tipSobe, Double minCena, Double maxCena);

    Optional<Soba> findByOpis(String opis);
    
}
