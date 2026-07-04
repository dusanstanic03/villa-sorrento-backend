package com.dusan.villa_sorrento_backend.repository;

import com.dusan.villa_sorrento_backend.model.Usluga;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository za pristup podacima o dodatnim uslugama.
 */
public interface UslugaRepository extends JpaRepository<Usluga, Long> {
    Optional<Usluga> findByNaziv(String naziv);

    List<Usluga> findByAktivna(Boolean aktivna);
}
