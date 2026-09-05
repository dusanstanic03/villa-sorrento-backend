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
 * periodu i filterima.Prilikom pretrage dostupnih soba proverava se i da li postoje
 * stavke rezervacije koje se vremenski preklapaju sa trazenim periodom.
 *
 * @author Dusan
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
     * Kreira novu sobu.
     * Metoda mapira DTO u domenski objekat sobe, pronalazi i postavlja tip sobe,
     * postavlja da je nova soba dostupna i cuva sobu u bazi. Tip sobe se moze
     * pronaci preko identifikatora tipa sobe ili preko naziva tipa sobe.
     *
     * @param sobaDTO podaci o sobi koja se kreira
     * @return sacuvana soba u formi DTO-a
     * @throws EntityNotFoundException ako tip sobe sa prosledjenim identifikatorom ili nazivom ne postoji
     * @throws IllegalArgumentException ako tip sobe nije prosledjen
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
     * Metoda prvo pronalazi sobu po identifikatoru. Ako soba postoji, vrednosti iz
     * DTO objekta se prenose na postojeci domenski objekat, azurira se veza sa
     * tipom sobe i izmenjena soba se cuva u bazi.
     *
     * @param id identifikator sobe koja se azurira
     * @param sobaDTO novi podaci o sobi
     * @return azurirana soba u formi DTO-a
     * @throws EntityNotFoundException ako soba sa zadatim identifikatorom ne postoji
     * @throws EntityNotFoundException ako tip sobe sa prosledjenim identifikatorom ili nazivom ne postoji
     * @throws IllegalArgumentException ako tip sobe nije prosledjen
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
     * Metoda proverava da li soba postoji u bazi. Ako postoji, soba se brise.
     * Ako ne postoji, baca se izuzetak.
     *
     * @param id identifikator sobe koja se brise
     * @throws EntityNotFoundException ako soba sa zadatim identifikatorom ne postoji
     */
    public void deleteSoba(Long id) {
        if (!sobaRepository.existsById(id)) {
            throw new EntityNotFoundException("Soba sa ID " + id + " nije pronađena.");
        }
        sobaRepository.deleteById(id);
    }

    /**
     * Vraca sve sobe iz sistema.
     * Metoda ucitava sve sobe iz baze, mapira ih u DTO objekte i vraca listu soba.
     * Koristi se za prikaz soba klijentima i administratorima.
     *
     * @return lista svih soba u formi DTO-a
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
     * Metoda pretrazuje bazu po identifikatoru sobe. Ako soba postoji, vraca se
     * njen DTO prikaz. Ako soba ne postoji, baca se izuzetak.
     *
     * @param id identifikator sobe koja se pretrazuje
     * @return pronadjena soba u formi DTO-a
     * @throws EntityNotFoundException ako soba sa zadatim identifikatorom ne postoji
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
     * Pretrazuje sobe dostupne za zadati period i opcione kriterijume.
     * Metoda prvo ucitava sobe koje su generalno oznacene kao dostupne. Zatim ih,
     * ako su kriterijumi prosledjeni, filtrira po tipu sobe, minimalnoj ceni i
     * maksimalnoj ceni. Nakon toga za svaku sobu proverava da li postoji stavka
     * rezervacije koja se vremenski preklapa sa trazenim periodom. U rezultat ulaze
     * samo sobe koje nemaju preklapajuce rezervacije.
     *
     * @param datumOd datum pocetka trazenog perioda boravka
     * @param datumDo datum kraja trazenog perioda boravka
     * @param tipSobe opcioni naziv tipa sobe po kom se filtrira
     * @param minCena opciona minimalna cena sobe
     * @param maxCena opciona maksimalna cena sobe
     * @return lista dostupnih soba koje ispunjavaju zadate kriterijume
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

    /**
     * Pronalazi tip sobe na osnovu podataka iz DTO objekta.
     *
     * Ako DTO sadrzi identifikator tipa sobe, tip se trazi po identifikatoru.
     * Ako identifikator nije prosledjen, tip sobe se trazi po nazivu. Ako nije
     * prosledjen ni identifikator ni naziv, baca se izuzetak.
     *
     * @param sobaDTO podaci o sobi koji sadrze identifikator ili naziv tipa sobe
     * @return pronadjeni tip sobe
     * @throws EntityNotFoundException ako tip sobe sa zadatim identifikatorom ili nazivom ne postoji
     * @throws IllegalArgumentException ako nije prosledjen ni identifikator ni naziv tipa sobe
     */
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
