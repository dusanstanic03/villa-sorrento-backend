package com.dusan.villa_sorrento_backend.service;

import com.dusan.villa_sorrento_backend.dto.UslugaDTO;
import com.dusan.villa_sorrento_backend.mapper.UslugaMapper;
import com.dusan.villa_sorrento_backend.model.Usluga;
import com.dusan.villa_sorrento_backend.repository.UslugaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Servis za sistemske operacije nad dodatnim uslugama.
 * Klasa sadrzi logiku za kreiranje, pregled, izmenu i brisanje usluga.
 * Usluge se mogu povezati sa stavkama rezervacije i mogu povecati ukupan iznos
 * rezervacije.
 *
 * @author Dusan
 */
@Service
public class UslugaService {
    private final UslugaRepository uslugaRepository;
    private final UslugaMapper uslugaMapper;

    public UslugaService(UslugaRepository uslugaRepository, UslugaMapper uslugaMapper) {
        this.uslugaRepository = uslugaRepository;
        this.uslugaMapper = uslugaMapper;
    }

    /**
     * Kreira novu uslugu.
     * Metoda proverava da li vec postoji usluga sa istim nazivom. Ako ne postoji,
     * DTO se mapira u domenski objekat usluge i cuva u bazi. Ako status aktivnosti
     * nije prosledjen, usluga se podrazumevano oznacava kao aktivna.
     *
     * @param uslugaDTO podaci o usluzi koja se kreira
     * @return sacuvana usluga predstavljena kao DTO
     * @throws IllegalArgumentException ako usluga sa istim nazivom vec postoji
     */
    public UslugaDTO createUsluga(UslugaDTO uslugaDTO) {
        if (uslugaRepository.findByNaziv(uslugaDTO.getNaziv()).isPresent()) {
            throw new IllegalArgumentException("Usluga sa nazivom " + uslugaDTO.getNaziv() + " vec postoji.");
        }
        if (uslugaDTO.getAktivna() == null) {
            uslugaDTO.setAktivna(true);
        }
        Usluga saved = uslugaRepository.save(uslugaMapper.uslugaDTOToUsluga(uslugaDTO));
        return uslugaMapper.uslugaToUslugaDTO(saved);
    }

    /**
     * Vraca sve usluge iz sistema.
     * Metoda ucitava sve usluge iz baze, bez obzira na njihov status aktivnosti,
     * mapira ih u DTO objekte i vraca listu.
     *
     * @return lista svih usluga predstavljenih kao DTO objekti
     */
    public List<UslugaDTO> getAllUsluge() {
        return uslugaRepository.findAll().stream()
                .map(uslugaMapper::uslugaToUslugaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Vraca sve aktivne usluge.
     * Metoda pronalazi samo usluge koje su oznacene kao aktivne. Aktivne usluge se
     * nude klijentima prilikom kreiranja rezervacije.
     *
     * @return lista aktivnih usluga predstavljenih kao DTO objekti
     */
    public List<UslugaDTO> getAktivneUsluge() {
        return uslugaRepository.findByAktivna(true).stream()
                .map(uslugaMapper::uslugaToUslugaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Pronalazi uslugu po identifikatoru.
     *
     * @param id identifikator usluge
     * @return pronadjena usluga
     */
    public UslugaDTO getUslugaById(Long id) {
        return uslugaRepository.findById(id)
                .map(uslugaMapper::uslugaToUslugaDTO)
                .orElseThrow(() -> new EntityNotFoundException("Usluga sa ID " + id + " nije pronadjena."));
    }

    /**
     * Azurira postojecu uslugu.
     * Metoda prvo pronalazi uslugu po identifikatoru. Ako usluga postoji, vrednosti
     * iz DTO objekta se prenose na postojeci domenski objekat i izmenjena usluga se
     * cuva u bazi.
     *
     * @param id identifikator usluge koja se azurira
     * @param uslugaDTO novi podaci o usluzi
     * @return azurirana usluga predstavljena kao DTO
     * @throws EntityNotFoundException ako usluga sa zadatim identifikatorom ne postoji
     */
    public UslugaDTO updateUsluga(Long id, UslugaDTO uslugaDTO) {
        Usluga usluga = uslugaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usluga sa ID " + id + " nije pronadjena."));
        uslugaMapper.updateUslugaFromDto(uslugaDTO, usluga);
        return uslugaMapper.uslugaToUslugaDTO(uslugaRepository.save(usluga));
    }

    /**
     * Brise uslugu po identifikatoru.
     * Metoda proverava da li usluga postoji u bazi. Ako postoji, usluga se brise.
     * Ako ne postoji, baca se izuzetak.
     *
     * @param id identifikator usluge koja se brise
     * @throws EntityNotFoundException ako usluga sa zadatim identifikatorom ne postoji
     */
    public void deleteUsluga(Long id) {
        if (!uslugaRepository.existsById(id)) {
            throw new EntityNotFoundException("Usluga sa ID " + id + " nije pronadjena.");
        }
        uslugaRepository.deleteById(id);
    }
}
