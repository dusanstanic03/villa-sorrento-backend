/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.service;

import com.dusan.villa_sorrento_backend.dto.SobaDTO;
import com.dusan.villa_sorrento_backend.mapper.SobaMapper;
import com.dusan.villa_sorrento_backend.model.Soba;
import com.dusan.villa_sorrento_backend.model.StavkaRezervacije;
import com.dusan.villa_sorrento_backend.repository.SobaRepository;
import com.dusan.villa_sorrento_backend.repository.StavkaRezervacijeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author Dusan
 */
@Service
public class SobaService {

    private final SobaRepository sobaRepository;
    private final SobaMapper sobaMapper;
    private final StavkaRezervacijeRepository stavkaRezervacijeRepository;

    public SobaService(SobaRepository sobaRepository, SobaMapper sobaMapper, StavkaRezervacijeRepository stavkaRezervacijeRepository) {
        this.sobaRepository = sobaRepository;
        this.sobaMapper = sobaMapper;
        this.stavkaRezervacijeRepository = stavkaRezervacijeRepository;
    }

    // SK6 - Kreiranje sobe (admin)
    public SobaDTO createSoba(SobaDTO sobaDTO) {
        Soba soba = sobaMapper.sobaDTOToSoba(sobaDTO);
        soba.setDostupna(true); // Nova soba je dostupna po default-u
        Soba savedSoba = sobaRepository.save(soba);
        return sobaMapper.sobaToSobaDTO(savedSoba);
    }

    // SK7: Izmena sobe (admin)
    public SobaDTO updateSoba(Long id, SobaDTO sobaDTO) {
        Soba existingSoba = sobaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Soba sa ID " + id + " nije pronađena."));

        sobaMapper.updateSobaFromDto(sobaDTO, existingSoba); // Koristi mapper za update
        Soba updatedSoba = sobaRepository.save(existingSoba);
        return sobaMapper.sobaToSobaDTO(updatedSoba);
    }

    // SK8: Brisanje sobe (admin)
    public void deleteSoba(Long id) {
        if (!sobaRepository.existsById(id)) {
            throw new EntityNotFoundException("Soba sa ID " + id + " nije pronađena.");
        }
        sobaRepository.deleteById(id);
    }

    // SK3: Pretraga soba - osnovna pretraga
    public List<SobaDTO> getAllSobe() {
        List<Soba> sobe = sobaRepository.findAll();
        List<SobaDTO> sobaDTOs = new ArrayList<>();

        for (Soba soba : sobe) {
            SobaDTO sobaDTO = sobaMapper.sobaToSobaDTO(soba);
            sobaDTOs.add(sobaDTO);
        }
        return sobaDTOs;
    }

    public SobaDTO getSobaById(Long id) {
        Optional<Soba> optionalSoba = sobaRepository.findById(id);

        if (optionalSoba.isPresent()) {
            Soba soba = optionalSoba.get();
            SobaDTO sobaDTO = sobaMapper.sobaToSobaDTO(soba);
            return sobaDTO;
        } else {
            throw new EntityNotFoundException("Soba sa ID " + id + " nije pronađena.");
        }
    }
        // SK3: Pretraga dostupnih soba za određeni period
    public List<SobaDTO> pretraziDostupneSobe(LocalDate datumOd, LocalDate datumDo, String tipSobe, Double minCena, Double maxCena) {
        List<Soba> sveSobe = sobaRepository.findByDostupna(true); // Krećemo od svih dostupnih soba

        if (tipSobe != null && !tipSobe.isEmpty()) {
            sveSobe = sveSobe.stream()
                    .filter(soba -> soba.getTipSobe().equalsIgnoreCase(tipSobe))
                    .collect(Collectors.toList());
        }
        if (minCena != null) {
            sveSobe = sveSobe.stream()
                    .filter(soba -> soba.getCena() >= minCena)
                    .collect(Collectors.toList());
        }
        if (maxCena != null) {
            sveSobe = sveSobe.stream()
                    .filter(soba -> soba.getCena() <= maxCena)
                    .collect(Collectors.toList());
        }

        // Filtriranje soba koje su zauzete u datom periodu
        List<Soba> dostupneSobe = sveSobe.stream()
            .filter(soba -> {
                // popravka: Koristimo novu metodu koja proverava preklapanje datuma
                List<StavkaRezervacije> zauzeteStavke = stavkaRezervacijeRepository.findOverlappingReservations(
                        soba.getIdSoba(), datumOd, datumDo);
                return zauzeteStavke.isEmpty(); // Soba je slobodna ako nema preklapajucih datuma
            }).collect(Collectors.toList());
            
        List<SobaDTO> dostupneSobeDTOs = new ArrayList<>();
        for (Soba soba : dostupneSobe) {
            SobaDTO sobaDTO = sobaMapper.sobaToSobaDTO(soba);
            dostupneSobeDTOs.add(sobaDTO);   
        }
        return dostupneSobeDTOs;
        
    }

}
