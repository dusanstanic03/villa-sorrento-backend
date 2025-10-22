/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.service;

/**
 *
 * @author Dusan
 */


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

    // Kreiranje novog plaćanja za rezervaciju
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

    // Ažuriranje statusa plaćanja (npr. iz PENDING u COMPLETED)
    @Transactional
    public PlacanjeDTO updateStatusPlacanja(Long placanjeId, String newStatus) {
        Placanje placanje = placanjeRepository.findById(placanjeId)
                .orElseThrow(() -> new EntityNotFoundException("Plaćanje sa ID " + placanjeId + " nije pronađeno."));

        placanje.setStatus(newStatus);
        Placanje updatedPlacanje = placanjeRepository.save(placanje);
        return placanjeMapper.placanjeToPlacanjeDTO(updatedPlacanje);
    }

    // Prikaz svih plaćanja za određenu rezervaciju
    public List<PlacanjeDTO> getPlacanjaByRezervacijaId(Long rezervacijaId) {
        return placanjeRepository.findByRezervacija_IdRezervacija(rezervacijaId).stream()
                .map(placanjeMapper::placanjeToPlacanjeDTO)
                .collect(Collectors.toList());
    }

    // Admin pregled svih plaćanja
    public List<PlacanjeDTO> getAllPlacanja() {
        return placanjeRepository.findAll().stream()
                .map(placanjeMapper::placanjeToPlacanjeDTO)
                .collect(Collectors.toList());
    }

    
    public PlacanjeDTO getPlacanjeById(Long id) {
        return placanjeRepository.findById(id)
                .map(placanjeMapper::placanjeToPlacanjeDTO)
                .orElseThrow(() -> new EntityNotFoundException("Plaćanje sa ID " + id + " nije pronađeno."));
    }

    // Calculate total paid for a reservation
    public Double calculateTotalPaidForReservation(Long rezervacijaId) {
        List<Placanje> placanja = placanjeRepository.findByRezervacija_IdRezervacija(rezervacijaId);
        return placanja.stream()
                .filter(p -> "COMPLETED".equalsIgnoreCase(p.getStatus()) || "PROCESSED".equalsIgnoreCase(p.getStatus())) // Only sum completed/processed payments
                .mapToDouble(Placanje::getIznos)
                .sum();
    }
}
