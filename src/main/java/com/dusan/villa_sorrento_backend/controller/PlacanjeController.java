/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.controller;

/**
 *
 * @author Dusan
 */


import com.dusan.villa_sorrento_backend.dto.PlacanjeDTO;
import com.dusan.villa_sorrento_backend.service.PlacanjeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/placanja")
public class PlacanjeController {
    
    private final PlacanjeService placanjeService;

    public PlacanjeController(PlacanjeService placanjeService) {
        this.placanjeService = placanjeService;
    }

    // Kreiranje novog plaćanja za određenu rezervaciju
    // Koristi se kada korisnik odabere način plaćanja za deo iznosa ili ceo iznos
    @PostMapping("/rezervacija/{rezervacijaId}")
    public ResponseEntity<PlacanjeDTO> createPlacanje(@PathVariable Long rezervacijaId, @RequestBody PlacanjeDTO placanjeDTO) {
        try {
            PlacanjeDTO novaPlacanje = placanjeService.createPlacanje(rezervacijaId, placanjeDTO);
            return new ResponseEntity<>(novaPlacanje, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Ažuriranje statusa plaćanja 
    @PutMapping("/{placanjeId}/status")
    public ResponseEntity<PlacanjeDTO> updatePlacanjeStatus(@PathVariable Long placanjeId, @RequestBody String newStatus) {
        try {
            PlacanjeDTO updatedPlacanje = placanjeService.updateStatusPlacanja(placanjeId, newStatus);
            return new ResponseEntity<>(updatedPlacanje, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Prikaz svih plaćanja za određenu rezervaciju
    @GetMapping("/rezervacija/{rezervacijaId}")
    public ResponseEntity<List<PlacanjeDTO>> getPlacanjaByRezervacijaId(@PathVariable Long rezervacijaId) {
        try {
            List<PlacanjeDTO> placanja = placanjeService.getPlacanjaByRezervacijaId(rezervacijaId);
            return new ResponseEntity<>(placanja, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Admin pregled svih plaćanja
    @GetMapping
    public ResponseEntity<List<PlacanjeDTO>> getAllPlacanja() {
        List<PlacanjeDTO> placanja = placanjeService.getAllPlacanja();
        return new ResponseEntity<>(placanja, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlacanjeDTO> getPlacanjeById(@PathVariable Long id) {
        try {
            PlacanjeDTO placanje = placanjeService.getPlacanjeById(id);
            return new ResponseEntity<>(placanje, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/rezervacija/{rezervacijaId}/total-paid")
    public ResponseEntity<Double> getTotalPaidForReservation(@PathVariable Long rezervacijaId) {
        try {
            Double totalPaid = placanjeService.calculateTotalPaidForReservation(rezervacijaId);
            return new ResponseEntity<>(totalPaid, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
