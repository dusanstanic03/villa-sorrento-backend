/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.dusan.villa_sorrento_backend.repository;

import com.dusan.villa_sorrento_backend.model.StavkaRezervacije;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Dusan
 */
public interface StavkaRezervacijeRepository extends JpaRepository<StavkaRezervacije, Long> {
    @Query("SELECT s FROM StavkaRezervacije s WHERE s.soba.idSoba = :sobaId " +
           "AND s.datumOd < :datumDo AND s.datumDo > :datumOd")
    List<StavkaRezervacije> findOverlappingReservations(
            @Param("sobaId") Long sobaId,
            @Param("datumOd") LocalDate datumOd,
            @Param("datumDo") LocalDate datumDo
    );
    
    
    default List<StavkaRezervacije> findBySobaIdSobaAndDatumDoAndDatumOd(
            Long sobaId, LocalDate datumOd, LocalDate datumDo) {
        return findOverlappingReservations(sobaId, datumOd, datumDo);
    }
}
