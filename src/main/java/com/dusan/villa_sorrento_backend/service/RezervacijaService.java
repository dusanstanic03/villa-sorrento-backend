/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.service;

import com.dusan.villa_sorrento_backend.dto.PlacanjeDTO;
import com.dusan.villa_sorrento_backend.dto.RezervacijaDTO;
import com.dusan.villa_sorrento_backend.dto.StavkaRezervacijeDTO;
import com.dusan.villa_sorrento_backend.mapper.RezervacijaMapper;
import com.dusan.villa_sorrento_backend.mapper.StavkaRezervacijeMapper;
import com.dusan.villa_sorrento_backend.model.Rezervacija;
import com.dusan.villa_sorrento_backend.model.Soba;
import com.dusan.villa_sorrento_backend.model.StavkaRezervacije;
import com.dusan.villa_sorrento_backend.model.Gost;
import com.dusan.villa_sorrento_backend.model.Usluga;
import com.dusan.villa_sorrento_backend.model.User;
import com.dusan.villa_sorrento_backend.repository.GostRepository;
import com.dusan.villa_sorrento_backend.repository.RezervacijaRepository;
import com.dusan.villa_sorrento_backend.repository.SobaRepository;
import com.dusan.villa_sorrento_backend.repository.StavkaRezervacijeRepository;
import com.dusan.villa_sorrento_backend.repository.UslugaRepository;
import com.dusan.villa_sorrento_backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Dusan
 */
@Service
public class RezervacijaService {
    private final RezervacijaRepository rezervacijaRepository;
    private final UserRepository userRepository;
    private final SobaRepository sobaRepository;
    private final StavkaRezervacijeRepository stavkaRezervacijeRepository;
    private final RezervacijaMapper rezervacijaMapper;
    private final StavkaRezervacijeMapper stavkaRezervacijeMapper;
    private final PlacanjeService placanjeService;
    private final GostRepository gostRepository;
    private final UslugaRepository uslugaRepository;
    
    public RezervacijaService(RezervacijaRepository rezervacijaRepository, 
            UserRepository userRepository, SobaRepository sobaRepository, 
            StavkaRezervacijeRepository stavkaRezervacijeRepository, 
            RezervacijaMapper rezervacijaMapper, StavkaRezervacijeMapper stavkaRezervacijeMapper, 
            PlacanjeService placanjeService, GostRepository gostRepository, UslugaRepository uslugaRepository) {
        this.rezervacijaRepository = rezervacijaRepository;
        this.userRepository = userRepository;
        this.sobaRepository = sobaRepository;
        this.stavkaRezervacijeRepository = stavkaRezervacijeRepository;
        this.rezervacijaMapper = rezervacijaMapper;
        this.stavkaRezervacijeMapper = stavkaRezervacijeMapper;
        this.placanjeService = placanjeService;
        this.gostRepository = gostRepository;
        this.uslugaRepository = uslugaRepository;
    }

    /**
     * Kreira rezervaciju za korisnika sa jednom ili vise stavki.
     *
     * Metoda proverava dostupnost soba, povezuje stavke sa gostima i uslugama,
     * racuna iznos svake stavke i ukupan iznos rezervacije.
     *
     * @param userId identifikator korisnika koji kreira rezervaciju
     * @param stavkeDTO stavke rezervacije
     * @return sacuvana rezervacija
     */
    @Transactional //treba nam za transakcije koje imaju vise operacija
    public RezervacijaDTO createRezervacija(Long userId, Set<StavkaRezervacijeDTO> stavkeDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Korisnik sa ID " + userId + " nije pronađen."));

        Rezervacija novaRezervacija = new Rezervacija();
        novaRezervacija.setUser(user);
        novaRezervacija.setDatumKreiranja(LocalDate.now());
        novaRezervacija.setStavkeRezervacije(new HashSet<>());
        novaRezervacija.setIznos(0.0); // Inicijalni iznos

        double ukupanIznos = 0.0;
        int rbBrojac = 1;

        for (StavkaRezervacijeDTO stavkaDTO : stavkeDTO) {
            Soba soba = sobaRepository.findById(stavkaDTO.getSobaId()).orElseThrow(() -> new EntityNotFoundException("Soba sa ID " + stavkaDTO.getSobaId() + " nije pronađena."));
            
            // Provera dostupnosti sobe za taj period
            List<StavkaRezervacije> conflictingStavke = stavkaRezervacijeRepository.findOverlappingReservations(
                soba.getIdSoba(), stavkaDTO.getDatumOd(), stavkaDTO.getDatumDo());
            if (!conflictingStavke.isEmpty()) {
                throw new IllegalArgumentException("Soba " + soba.getOpis() + " je zauzeta u periodu od " + stavkaDTO.getDatumOd() + " do " + stavkaDTO.getDatumDo());
            }

            StavkaRezervacije stavka = stavkaRezervacijeMapper.stavkaRezervacijeDTOToStavkaRezervacije(stavkaDTO);
            stavka.setRezervacija(novaRezervacija); // Postavljamo referencu na rezervaciju
            stavka.setSoba(soba); // Postavljamo referencu na sobu
            stavka.setRb(rbBrojac++);
            stavka.setGosti(resolveGosti(stavkaDTO.getGostIds()));
            stavka.setUsluge(resolveUsluge(stavkaDTO.getUslugaIds()));

            // Izračunaj iznos stavke
            long brojNocenja = ChronoUnit.DAYS.between(stavka.getDatumOd(), stavka.getDatumDo());
            double iznosUsluga = stavka.getUsluge().stream().mapToDouble(Usluga::getCena).sum();
            double iznosStavke = brojNocenja * soba.getCena() + iznosUsluga;
            stavka.setIznos(iznosStavke);
            ukupanIznos += iznosStavke;

            novaRezervacija.getStavkeRezervacije().add(stavka);
        }

        novaRezervacija.setIznos(ukupanIznos);
        Rezervacija savedRezervacija = rezervacijaRepository.save(novaRezervacija);
        return rezervacijaMapper.rezervacijaToRezervacijaDTO(savedRezervacija);
    }

    private Set<Gost> resolveGosti(Set<Long> gostIds) {
        if (gostIds == null || gostIds.isEmpty()) {
            return new HashSet<>();
        }
        return gostIds.stream()
                .map(id -> gostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gost sa ID " + id + " nije pronađen.")))
                .collect(Collectors.toSet());
    }

    private Set<Usluga> resolveUsluge(Set<Long> uslugaIds) {
        if (uslugaIds == null || uslugaIds.isEmpty()) {
            return new HashSet<>();
        }
        return uslugaIds.stream()
                .map(id -> uslugaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usluga sa ID " + id + " nije pronađena.")))
                .collect(Collectors.toSet());
    }
    
    // SK13: Pretraga rezervacija (admin, klijent)
    public List<RezervacijaDTO> getAllRezervacije() {
        return rezervacijaRepository.findAll().stream()
                .map(rezervacijaMapper::rezervacijaToRezervacijaDTO)
                .collect(Collectors.toList());
    }

    public RezervacijaDTO getRezervacijaById(Long id) {
        return rezervacijaRepository.findById(id)
                .map(rezervacijaMapper::rezervacijaToRezervacijaDTO)
                .orElseThrow(() -> new EntityNotFoundException("Rezervacija sa ID " + id + " nije pronađena."));
    }

    public List<RezervacijaDTO> getRezervacijeByUserId(Long userId) {
        return rezervacijaRepository.findByUserIdUser(userId).stream()
                .map(rezervacijaMapper::rezervacijaToRezervacijaDTO)
                .collect(Collectors.toList());
    }

    // SK14: Poništavanje rezervacije (admin, klijent)
    @Transactional
    public void cancelRezervacija(Long rezervacijaId) {
        Rezervacija rezervacija = rezervacijaRepository.findById(rezervacijaId)
                .orElseThrow(() -> new EntityNotFoundException("Rezervacija sa ID " + rezervacijaId + " nije pronađena."));
        //brišemo rezervaciju, što će automatski obrisati i stavke i plaćanja zbog cascade
        rezervacijaRepository.delete(rezervacija);
    }
    
    
    @Transactional
    public PlacanjeDTO addPaymentToRezervacija(Long rezervacijaId, PlacanjeDTO paymentDto) {
        // delegira se na PlacanjeService
        return placanjeService.createPlacanje(rezervacijaId, paymentDto);
    }
    
}
