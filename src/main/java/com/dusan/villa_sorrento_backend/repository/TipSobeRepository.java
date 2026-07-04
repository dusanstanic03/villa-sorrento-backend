package com.dusan.villa_sorrento_backend.repository;

import com.dusan.villa_sorrento_backend.model.TipSobe;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository za pristup podacima o tipovima soba.
 */
public interface TipSobeRepository extends JpaRepository<TipSobe, Long> {
    Optional<TipSobe> findByNaziv(String naziv);
}
