/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.service;

import com.dusan.villa_sorrento_backend.dto.PlacanjeDTO;
import com.dusan.villa_sorrento_backend.mapper.PlacanjeMapper;
import com.dusan.villa_sorrento_backend.model.Placanje;
import com.dusan.villa_sorrento_backend.model.Rezervacija;
import com.dusan.villa_sorrento_backend.repository.PlacanjeRepository;
import com.dusan.villa_sorrento_backend.repository.RezervacijaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servis za sistemske operacije sa placanjima.
 * Obuhvata evidentiranje placanja, promenu statusa, pregled placanja i obracun
 * ukupno placenog iznosa za rezervaciju.
 *
 * @author Dusan
 */
@Service
public class PlacanjeService {
    private final PlacanjeRepository placanjeRepository;
    private final RezervacijaRepository rezervacijaRepository;
    private final PlacanjeMapper placanjeMapper;

    public PlacanjeService(PlacanjeRepository placanjeRepository, RezervacijaRepository rezervacijaRepository, PlacanjeMapper placanjeMapper) {
        this.placanjeRepository = placanjeRepository;
        this.rezervacijaRepository = rezervacijaRepository;
        this.placanjeMapper = placanjeMapper;
    }

    /**
     * Kreira novo placanje za rezervaciju.
     * Metoda prvo pronalazi rezervaciju za koju se evidentira placanje. Zatim mapira
     * DTO u domenski objekat placanja, povezuje placanje sa rezervacijom i postavlja
     * datum placanja na danasnji datum. Ako status placanja nije prosledjen, postavlja
     * se podrazumevani status PENDING. Nakon cuvanja u bazi vraca se DTO sacuvanog
     * placanja.
     *
     * @param rezervacijaId identifikator rezervacije za koju se evidentira placanje
     * @param placanjeDTO podaci o placanju koje treba evidentirati
     * @return sacuvano placanje u formi DTO-a
     * @throws EntityNotFoundException ako rezervacija sa zadatim identifikatorom ne postoji
     */
    @Transactional
    public PlacanjeDTO createPlacanje(Long rezervacijaId, PlacanjeDTO placanjeDTO) {
        Rezervacija rezervacija = rezervacijaRepository.findById(rezervacijaId)
                .orElseThrow(() -> new EntityNotFoundException("Rezervacija sa ID " + rezervacijaId + " nije pronađena."));

        Placanje placanje = placanjeMapper.placanjeDTOToPlacanje(placanjeDTO);
        placanje.setRezervacija(rezervacija);
        placanje.setDatumPlacanja(LocalDate.now()); 

        // default status if not provided ("PENDING" for cash, "COMPLETED" for card/crypto if successful)
        if (placanje.getStatus() == null || placanje.getStatus().isEmpty()) {
            placanje.setStatus("PENDING"); 
        }

        Placanje savedPlacanje = placanjeRepository.save(placanje);
        return placanjeMapper.placanjeToPlacanjeDTO(savedPlacanje);
    }

    /**
     * Azurira status postojeceg placanja.
     * Metoda pronalazi placanje po identifikatoru, postavlja novi status i cuva
     * izmenjeno placanje u bazi. Koristi se kada se status placanja promeni, na
     * primer iz PENDING u COMPLETED.
     *
     * @param placanjeId identifikator placanja koje se azurira
     * @param newStatus novi status placanja
     * @return azurirano placanje u formi DTO-a
     * @throws EntityNotFoundException ako placanje sa zadatim identifikatorom ne postoji
     */
    @Transactional
    public PlacanjeDTO updateStatusPlacanja(Long placanjeId, String newStatus) {
        Placanje placanje = placanjeRepository.findById(placanjeId)
                .orElseThrow(() -> new EntityNotFoundException("Plaćanje sa ID " + placanjeId + " nije pronađeno."));

        placanje.setStatus(newStatus);
        Placanje updatedPlacanje = placanjeRepository.save(placanje);
        return placanjeMapper.placanjeToPlacanjeDTO(updatedPlacanje);
    }

    /**
     * Vraca sva placanja za odredjenu rezervaciju.
     * Metoda pronalazi placanja povezana sa rezervacijom ciji je identifikator
     * prosledjen i mapira ih u DTO objekte.
     *
     * @param rezervacijaId identifikator rezervacije za koju se pretrazuju placanja
     * @return lista placanja za zadatu rezervaciju
     */
    public List<PlacanjeDTO> getPlacanjaByRezervacijaId(Long rezervacijaId) {
        return placanjeRepository.findByRezervacija_IdRezervacija(rezervacijaId).stream()
                .map(placanjeMapper::placanjeToPlacanjeDTO)
                .collect(Collectors.toList());
    }

    /**
     * Vraca sva placanja iz sistema.
     * Metoda ucitava sva placanja iz baze, mapira ih u DTO objekte i vraca listu.
     * Koristi se za pregled placanja od strane admina.
     *
     * @return lista svih placanja u formi DTO objekata
     */
    public List<PlacanjeDTO> getAllPlacanja() {
        return placanjeRepository.findAll().stream()
                .map(placanjeMapper::placanjeToPlacanjeDTO)
                .collect(Collectors.toList());
    }


    /**
     * Pronalazi placanje po identifikatoru.
     * Metoda pretrazuje bazu po identifikatoru placanja. Ako placanje postoji,
     * vraca se njegov DTO prikaz. Ako ne postoji, baca se izuzetak.
     *
     * @param id identifikator placanja koje se pretrazuje
     * @return pronadjeno placanje predstavljeno kao DTO
     * @throws EntityNotFoundException ako placanje sa zadatim identifikatorom ne postoji
     */
    public PlacanjeDTO getPlacanjeById(Long id) {
        return placanjeRepository.findById(id)
                .map(placanjeMapper::placanjeToPlacanjeDTO)
                .orElseThrow(() -> new EntityNotFoundException("Plaćanje sa ID " + id + " nije pronađeno."));
    }

    /**
     * Racuna ukupan iznos uspesno obradjenih placanja za rezervaciju.
     * Metoda ucitava sva placanja za zadatu rezervaciju i sabira samo placanja
     * ciji je status COMPLETED ili PROCESSED. Placanja sa drugim statusima se ne
     * ukljucuju u zbir.
     *
     * @param rezervacijaId identifikator rezervacije za koju se racuna placeni iznos
     * @return ukupan iznos uspesno obradjenih placanja
     */
    public Double calculateTotalPaidForReservation(Long rezervacijaId) {
        List<Placanje> placanja = placanjeRepository.findByRezervacija_IdRezervacija(rezervacijaId);
        return placanja.stream()
                .filter(p -> "COMPLETED".equalsIgnoreCase(p.getStatus()) || "PROCESSED".equalsIgnoreCase(p.getStatus())) // Only sum completed/processed payments
                .mapToDouble(Placanje::getIznos)
                .sum();
    }
}
