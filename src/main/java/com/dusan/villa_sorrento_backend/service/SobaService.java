/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.service;

import com.dusan.villa_sorrento_backend.dto.SobaDTO;
import com.dusan.villa_sorrento_backend.mapper.SobaMapper;
import com.dusan.villa_sorrento_backend.model.Soba;
import com.dusan.villa_sorrento_backend.model.StavkaRezervacije;
import com.dusan.villa_sorrento_backend.model.TipSobe;
import com.dusan.villa_sorrento_backend.repository.SobaRepository;
import com.dusan.villa_sorrento_backend.repository.StavkaRezervacijeRepository;
import com.dusan.villa_sorrento_backend.repository.TipSobeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Servis za sistemske operacije sa sobama.
 * Obuhvata kreiranje, izmenu, brisanje, pregled i pretragu dostupnih soba po
 * periodu i filterima.
 */
@Service
public class SobaService {

    private final SobaRepository sobaRepository;
    private final SobaMapper sobaMapper;
    private final StavkaRezervacijeRepository stavkaRezervacijeRepository;
    private final TipSobeRepository tipSobeRepository;

    public SobaService(SobaRepository sobaRepository, SobaMapper sobaMapper, StavkaRezervacijeRepository stavkaRezervacijeRepository, TipSobeRepository tipSobeRepository) {
        this.sobaRepository = sobaRepository;
        this.sobaMapper = sobaMapper;
        this.stavkaRezervacijeRepository = stavkaRezervacijeRepository;
        this.tipSobeRepository = tipSobeRepository;
    }

    /**
     * Kreira novu sobu i povezuje je sa izabranim tipom sobe.
     *
     * @param sobaDTO podaci o sobi
     * @return sacuvana soba
     */
    public SobaDTO createSoba(SobaDTO sobaDTO) {
        Soba soba = sobaMapper.sobaDTOToSoba(sobaDTO);
        soba.setTipSobe(resolveTipSobe(sobaDTO));
        soba.setDostupna(true); // Nova soba je dostupna po default-u
        Soba savedSoba = sobaRepository.save(soba);
        return sobaMapper.sobaToSobaDTO(savedSoba);
    }

    /**
     * Azurira postojecu sobu.
     *
     * @param id identifikator sobe
     * @param sobaDTO novi podaci o sobi
     * @return azurirana soba
     */
    public SobaDTO updateSoba(Long id, SobaDTO sobaDTO) {
        Soba existingSoba = sobaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Soba sa ID " + id + " nije pronađena."));

        sobaMapper.updateSobaFromDto(sobaDTO, existingSoba); // Koristi mapper za update
        existingSoba.setTipSobe(resolveTipSobe(sobaDTO));
        Soba updatedSoba = sobaRepository.save(existingSoba);
        return sobaMapper.sobaToSobaDTO(updatedSoba);
    }

    /**
     * Brise sobu po identifikatoru.
     *
     * @param id identifikator sobe
     */
    public void deleteSoba(Long id) {
        if (!sobaRepository.existsById(id)) {
            throw new EntityNotFoundException("Soba sa ID " + id + " nije pronađena.");
        }
        sobaRepository.deleteById(id);
    }

    /**
     * Vraca sve sobe.
     *
     * @return lista soba
     */
    public List<SobaDTO> getAllSobe() {
        List<Soba> sobe = sobaRepository.findAll();
        List<SobaDTO> sobaDTOs = new ArrayList<>();

        for (Soba soba : sobe) {
            SobaDTO sobaDTO = sobaMapper.sobaToSobaDTO(soba);
            sobaDTOs.add(sobaDTO);
        }
        return sobaDTOs;
    }

    /**
     * Pronalazi sobu po identifikatoru.
     *
     * @param id identifikator sobe
     * @return pronadjena soba
     */
    public SobaDTO getSobaById(Long id) {
        Optional<Soba> optionalSoba = sobaRepository.findById(id);

        if (optionalSoba.isPresent()) {
            Soba soba = optionalSoba.get();
            return sobaMapper.sobaToSobaDTO(soba);
        } else {
            throw new EntityNotFoundException("Soba sa ID " + id + " nije pronađena.");
        }
    }
    /**
     * Pretrazuje dostupne sobe za zadati period i kriterijume.
     *
     * @param datumOd datum pocetka boravka
     * @param datumDo datum kraja boravka
     * @param tipSobe opcioni naziv tipa sobe
     * @param minCena minimalna cena
     * @param maxCena maksimalna cena
     * @return lista dostupnih soba
     */
    public List<SobaDTO> pretraziDostupneSobe(LocalDate datumOd, LocalDate datumDo, String tipSobe, Double minCena, Double maxCena) {
        List<Soba> sveSobe = sobaRepository.findByDostupna(true); // Krećemo od svih dostupnih soba

        if (tipSobe != null && !tipSobe.isEmpty()) {
            sveSobe = sveSobe.stream()
                    .filter(soba -> soba.getTipSobe() != null && soba.getTipSobe().getNaziv().equalsIgnoreCase(tipSobe))
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
            }).toList();
            
        List<SobaDTO> dostupneSobeDTOs = new ArrayList<>();
        for (Soba soba : dostupneSobe) {
            SobaDTO sobaDTO = sobaMapper.sobaToSobaDTO(soba);
            dostupneSobeDTOs.add(sobaDTO);   
        }
        return dostupneSobeDTOs;
        
    }

    private TipSobe resolveTipSobe(SobaDTO sobaDTO) {
        if (sobaDTO.getTipSobeId() != null) {
            return tipSobeRepository.findById(sobaDTO.getTipSobeId())
                    .orElseThrow(() -> new EntityNotFoundException("Tip sobe sa ID " + sobaDTO.getTipSobeId() + " nije pronađen."));
        }
        String naziv = sobaDTO.getTipSobeNaziv();
        if (naziv == null || naziv.isBlank()) {
            throw new IllegalArgumentException("Tip sobe je obavezan.");
        }
        return tipSobeRepository.findByNaziv(naziv)
                .orElseThrow(() -> new EntityNotFoundException("Tip sobe sa nazivom " + naziv + " nije pronađen."));
    }

}
