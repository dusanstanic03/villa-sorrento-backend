package com.dusan.villa_sorrento_backend.repository;

import com.dusan.villa_sorrento_backend.model.Gost;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository za pristup podacima o gostima.
 */
public interface GostRepository extends JpaRepository<Gost, Long> {
    Optional<Gost> findByBrojIsprave(String brojIsprave);
}
