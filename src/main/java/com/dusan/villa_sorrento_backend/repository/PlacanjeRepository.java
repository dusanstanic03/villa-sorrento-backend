/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.dusan.villa_sorrento_backend.repository;

import com.dusan.villa_sorrento_backend.model.Placanje;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dusan
 */
@Repository
public interface PlacanjeRepository extends JpaRepository<Placanje, Long>{
    List<Placanje> findByRezervacija_IdRezervacija(Long rezervacijaId);
  
    List<Placanje> findByRezervacija_IdRezervacijaAndStatus(Long rezervacijaId, String status);
  
    List<Placanje> findByRezervacija_IdRezervacijaAndNacinPlacanja(Long rezervacijaId, String nacinPlacanja);
   
    List<Placanje> findByDatumPlacanjaBetween(LocalDate startDate, LocalDate endDate);
   
    List<Placanje> findByStatus(String status);
 
    List<Placanje> findByNacinPlacanja(String nacinPlacanja);
}
