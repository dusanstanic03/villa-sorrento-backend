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
 * Servis za sistemske operacije sa rezervacijama.
 * Klasa sadrzi logiku za kreiranje rezervacije, proveru zauzetosti
 * sobe, povezivanje stavki rezervacije sa gostima i uslugama, obracun iznosa,
 * pregled rezervacija, ponistavanje rezervacije i dodavanje placanja.
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
     * Kreira rezervaciju za korisnika sa jednom ili vise stavki rezervacije.
     *
     * Metoda prvo pronalazi korisnika koji kreira rezervaciju. Za svaku stavku
     * rezervacije pronalazi sobu, proverava da li je soba slobodna u trazenom
     * periodu, povezuje stavku sa gostima i uslugama, postavlja redni broj stavke
     * i racuna iznos stavke. Iznos stavke se racuna kao broj nocenja pomnozen
     * cenom sobe, uvecan za zbir cena izabranih usluga. Na kraju se racuna ukupan
     * iznos rezervacije i rezervacija se cuva u bazi.
     *
     * @param userId identifikator korisnika koji kreira rezervaciju
     * @param stavkeDTO skup stavki rezervacije koje treba kreirati
     * @return sacuvana rezervacija predstavljena u formi DTO-a
     * @throws EntityNotFoundException ako korisnik sa zadatim identifikatorom ne postoji
     * @throws EntityNotFoundException ako neka od soba iz stavki ne postoji
     * @throws EntityNotFoundException ako neki od gostiju iz stavki ne postoji
     * @throws EntityNotFoundException ako neka od usluga iz stavki ne postoji
     * @throws IllegalArgumentException ako je neka soba zauzeta u trazenom periodu
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

    /**
     * Pronalazi goste na osnovu prosledjenih identifikatora.
     *
     * Ako skup identifikatora nije prosledjen ili je prazan, metoda vraca prazan
     * skup. U suprotnom, svaki identifikator se proverava u bazi i pronadjeni gosti
     * se vracaju kao skup.
     *
     * @param gostIds identifikatori gostiju koji se povezuju sa stavkom rezervacije
     * @return skup pronadjenih gostiju
     * @throws EntityNotFoundException ako neki gost sa prosledjenim identifikatorom ne postoji
     */
    private Set<Gost> resolveGosti(Set<Long> gostIds) {
        if (gostIds == null || gostIds.isEmpty()) {
            return new HashSet<>();
        }
        return gostIds.stream()
                .map(id -> gostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Gost sa ID " + id + " nije pronađen.")))
                .collect(Collectors.toSet());
    }

    /**
     * Pronalazi usluge na osnovu prosledjenih identifikatora.
     * Ako skup identifikatora nije prosledjen ili je prazan, metoda vraca prazan
     * skup. U suprotnom, svaki identifikator se proverava u bazi i pronadjene usluge
     * se vracaju kao skup.
     *
     * @param uslugaIds identifikatori usluga koje se povezuju sa stavkom rezervacije
     * @return skup pronadjenih usluga
     * @throws EntityNotFoundException ako neka usluga sa prosledjenim identifikatorom ne postoji
     */
    private Set<Usluga> resolveUsluge(Set<Long> uslugaIds) {
        if (uslugaIds == null || uslugaIds.isEmpty()) {
            return new HashSet<>();
        }
        return uslugaIds.stream()
                .map(id -> uslugaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usluga sa ID " + id + " nije pronađena.")))
                .collect(Collectors.toSet());
    }

    /**
     * Vraca sve rezervacije iz sistema.
     * Metoda ucitava sve rezervacije iz baze, mapira ih u DTO objekte i vraca listu.
     * Koristi se za pregled rezervacija od strane admina.
     *
     * @return lista svih rezervacija predstavljenih u formi DTO objekata
     */
    public List<RezervacijaDTO> getAllRezervacije() {
        return rezervacijaRepository.findAll().stream()
                .map(rezervacijaMapper::rezervacijaToRezervacijaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Pronalazi rezervaciju po identifikatoru.
     * Metoda pretrazuje bazu po identifikatoru rezervacije. Ako rezervacija postoji,
     * vraca se njen DTO prikaz. Ako ne postoji, baca se izuzetak.
     *
     * @param id identifikator rezervacije koja se pretrazuje
     * @return pronadjena rezervacija predstavljena kao DTO
     * @throws EntityNotFoundException ako rezervacija sa zadatim identifikatorom ne postoji
     */
    public RezervacijaDTO getRezervacijaById(Long id) {
        return rezervacijaRepository.findById(id)
                .map(rezervacijaMapper::rezervacijaToRezervacijaDTO)
                .orElseThrow(() -> new EntityNotFoundException("Rezervacija sa ID " + id + " nije pronađena."));
    }

    /**
     * Vraca rezervacije koje pripadaju odredjenom korisniku.
     * Metoda pronalazi sve rezervacije od korisnika sa prosledjenim
     * identifikatorom i mapira ih u DTO objekte. Koristi se za prikaz rezervacija
     * prijavljenog klijenta.
     *
     * @param userId identifikator korisnika cije se rezervacije pretrazuju
     * @return lista rezervacija korisnika u formi DTO objekata
     */
    public List<RezervacijaDTO> getRezervacijeByUserId(Long userId) {
        return rezervacijaRepository.findByUserIdUser(userId).stream()
                .map(rezervacijaMapper::rezervacijaToRezervacijaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Ponistava rezervaciju.
     * Metoda pronalazi rezervaciju po identifikatoru i brise je iz baze. Zbog
     * kaskadnog brisanja na domenskoj klasi, zajedno sa rezervacijom brisu se i
     * povezane stavke rezervacije i placanja.
     *
     * @param rezervacijaId identifikator rezervacije koja se ponistava
     * @throws EntityNotFoundException ako rezervacija sa zadatim identifikatorom ne postoji
     */
    @Transactional
    public void cancelRezervacija(Long rezervacijaId) {
        Rezervacija rezervacija = rezervacijaRepository.findById(rezervacijaId)
                .orElseThrow(() -> new EntityNotFoundException("Rezervacija sa ID " + rezervacijaId + " nije pronađena."));
        //brišemo rezervaciju, što će automatski obrisati i stavke i plaćanja zbog cascade
        rezervacijaRepository.delete(rezervacija);
    }


    /**
     * Dodaje placanje na postojecu rezervaciju.
     * Metoda delegira kreiranje placanja servisu za placanja. Rezervacija se
     * identifikuje preko prosledjenog identifikatora, a podaci o placanju se
     * prosledjuju kroz DTO objekat.
     *
     * @param rezervacijaId identifikator rezervacije za koju se evidentira placanje
     * @param paymentDto podaci o placanju koje treba evidentirati
     * @return sacuvano placanje predstavljeno kao DTO
     * @throws EntityNotFoundException ako rezervacija sa zadatim identifikatorom ne postoji
     */
    @Transactional
    public PlacanjeDTO addPaymentToRezervacija(Long rezervacijaId, PlacanjeDTO paymentDto) {
        // delegira se na PlacanjeService
        return placanjeService.createPlacanje(rezervacijaId, paymentDto);
    }
    
}
