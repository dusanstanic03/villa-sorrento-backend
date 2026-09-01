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
 * Servis za sistemske operacije sa uslugama.
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
     * Kreira novu dodatnu uslugu.
     *
     * @param uslugaDTO podaci o usluzi
     * @return sacuvana usluga
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
     * Vraca sve usluge.
     *
     * @return lista usluga
     */
    public List<UslugaDTO> getAllUsluge() {
        return uslugaRepository.findAll().stream()
                .map(uslugaMapper::uslugaToUslugaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Vraca aktivne usluge koje se nude klijentima.
     *
     * @return lista aktivnih usluga
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
     * Azurira dodatnu uslugu.
     *
     * @param id identifikator usluge
     * @param uslugaDTO novi podaci
     * @return azurirana usluga
     */
    public UslugaDTO updateUsluga(Long id, UslugaDTO uslugaDTO) {
        Usluga usluga = uslugaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usluga sa ID " + id + " nije pronadjena."));
        uslugaMapper.updateUslugaFromDto(uslugaDTO, usluga);
        return uslugaMapper.uslugaToUslugaDTO(uslugaRepository.save(usluga));
    }

    /**
     * Brise dodatnu uslugu ako postoji.
     *
     * @param id identifikator usluge
     */
    public void deleteUsluga(Long id) {
        if (!uslugaRepository.existsById(id)) {
            throw new EntityNotFoundException("Usluga sa ID " + id + " nije pronadjena.");
        }
        uslugaRepository.deleteById(id);
    }
}
