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
     *
     * @param rezervacijaId identifikator rezervacije
     * @param placanjeDTO podaci o placanju
     * @return sacuvano placanje
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
     *
     * @param placanjeId identifikator placanja
     * @param newStatus novi status placanja
     * @return azurirano placanje
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
     *
     * @param rezervacijaId identifikator rezervacije
     * @return lista placanja za rezervaciju
     */
    public List<PlacanjeDTO> getPlacanjaByRezervacijaId(Long rezervacijaId) {
        return placanjeRepository.findByRezervacija_IdRezervacija(rezervacijaId).stream()
                .map(placanjeMapper::placanjeToPlacanjeDTO)
                .collect(Collectors.toList());
    }

    /**
     * Vraca sva placanja u sistemu.
     *
     * @return lista placanja
     */
    public List<PlacanjeDTO> getAllPlacanja() {
        return placanjeRepository.findAll().stream()
                .map(placanjeMapper::placanjeToPlacanjeDTO)
                .collect(Collectors.toList());
    }

    
    /**
     * Pronalazi placanje po identifikatoru.
     *
     * @param id identifikator placanja
     * @return pronadjeno placanje
     */
    public PlacanjeDTO getPlacanjeById(Long id) {
        return placanjeRepository.findById(id)
                .map(placanjeMapper::placanjeToPlacanjeDTO)
                .orElseThrow(() -> new EntityNotFoundException("Plaćanje sa ID " + id + " nije pronađeno."));
    }

    /**
     * Racuna ukupan iznos uspesno obradjenih placanja za rezervaciju.
     *
     * @param rezervacijaId identifikator rezervacije
     * @return ukupno placeni iznos
     */
    public Double calculateTotalPaidForReservation(Long rezervacijaId) {
        List<Placanje> placanja = placanjeRepository.findByRezervacija_IdRezervacija(rezervacijaId);
        return placanja.stream()
                .filter(p -> "COMPLETED".equalsIgnoreCase(p.getStatus()) || "PROCESSED".equalsIgnoreCase(p.getStatus())) // Only sum completed/processed payments
                .mapToDouble(Placanje::getIznos)
                .sum();
    }
}
