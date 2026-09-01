/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.controller;


import com.dusan.villa_sorrento_backend.dto.PlacanjeDTO;
import com.dusan.villa_sorrento_backend.dto.RezervacijaDTO;
import com.dusan.villa_sorrento_backend.dto.StavkaRezervacijeDTO;
import com.dusan.villa_sorrento_backend.service.RezervacijaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Set;

/**
 * @author Dusan
 * REST kontroler za upravljanje rezervacijama.
 */

@RestController
@RequestMapping("/api/rezervacije")
public class RezervacijaController {
    private final RezervacijaService rezervacijaService;

    public RezervacijaController(RezervacijaService rezervacijaService) {
        this.rezervacijaService = rezervacijaService;
    }

    // SK4: Kreiranje rezervacije
    // Zahteva se userId i set stavki rezervacije
    @PostMapping("/{userId}")
    public ResponseEntity<RezervacijaDTO> createRezervacija(@PathVariable Long userId, @RequestBody Set<StavkaRezervacijeDTO> stavkeDTO) {
   
        try {
            RezervacijaDTO novaRezervacija = rezervacijaService.createRezervacija(userId, stavkeDTO);
            return new ResponseEntity<>(novaRezervacija, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Npr. soba je zauzeta
        }
    }

    // SK13: Pretraga svih rezervacija (Admin)
    @GetMapping
    public ResponseEntity<List<RezervacijaDTO>> getAllRezervacije() {
        List<RezervacijaDTO> rezervacije = rezervacijaService.getAllRezervacije();
        return new ResponseEntity<>(rezervacije, HttpStatus.OK);
    }

    // SK13: Pretraga rezervacija po korisniku (Klijent, Admin)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RezervacijaDTO>> getRezervacijeByUserId(@PathVariable Long userId) {
        List<RezervacijaDTO> rezervacije = rezervacijaService.getRezervacijeByUserId(userId);
        return new ResponseEntity<>(rezervacije, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RezervacijaDTO> getRezervacijaById(@PathVariable Long id) {
        try {
            RezervacijaDTO rezervacija = rezervacijaService.getRezervacijaById(id);
            return new ResponseEntity<>(rezervacija, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // SK14: Poništavanje rezervacije
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelRezervacija(@PathVariable Long id) {
        try {
            rezervacijaService.cancelRezervacija(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/{rezervacijaId}/placanja")
    public ResponseEntity<PlacanjeDTO> addPaymentToRezervacija(@PathVariable Long rezervacijaId, @RequestBody PlacanjeDTO placanjeDTO) {
        try {
            PlacanjeDTO newPayment = rezervacijaService.addPaymentToRezervacija(rezervacijaId, placanjeDTO);
            return new ResponseEntity<>(newPayment, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
