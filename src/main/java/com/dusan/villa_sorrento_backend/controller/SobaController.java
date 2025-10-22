/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dusan.villa_sorrento_backend.controller;

/**
 *
 * @author Dusan
 */

import com.dusan.villa_sorrento_backend.dto.SobaDTO;
import com.dusan.villa_sorrento_backend.service.SobaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/sobe")
public class SobaController {
    private final SobaService sobaService;

    public SobaController(SobaService sobaService) {
        this.sobaService = sobaService;
    }
    
    // SK6: Kreiranje sobe (Admin)
    @PostMapping
    public ResponseEntity<SobaDTO> createSoba(@RequestBody SobaDTO sobaDTO) {
        SobaDTO newSoba = sobaService.createSoba(sobaDTO);
        return new ResponseEntity<>(newSoba, HttpStatus.CREATED);
    }

    // SK7: Izmena sobe (Admin)
    @PutMapping("/{id}")
    public ResponseEntity<SobaDTO> updateSoba(@PathVariable Long id, @RequestBody SobaDTO sobaDTO) {
        try {
            SobaDTO updatedSoba = sobaService.updateSoba(id, sobaDTO);
            return new ResponseEntity<>(updatedSoba, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // SK8: Brisanje sobe (Admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSoba(@PathVariable Long id) {
        try {
            sobaService.deleteSoba(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // SK3: Pretraga soba (svi, klijent, admin)
    @GetMapping
    public ResponseEntity<List<SobaDTO>> getAllSobe() {
        List<SobaDTO> sobe = sobaService.getAllSobe();
        return new ResponseEntity<>(sobe, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SobaDTO> getSobaById(@PathVariable Long id) {
        try {
            SobaDTO soba = sobaService.getSobaById(id);
            return new ResponseEntity<>(soba, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // SK3: Pretraga dostupnih soba za određeni period i kriterijume
    @GetMapping("/pretrazi-dostupne")
    public ResponseEntity<List<SobaDTO>> pretraziDostupneSobe(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datumOd,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datumDo,
            @RequestParam(required = false) String tipSobe,
            @RequestParam(required = false) Double minCena,
            @RequestParam(required = false) Double maxCena){
            
        List<SobaDTO> dostupneSobe = sobaService.pretraziDostupneSobe(datumOd, datumDo, tipSobe, minCena, maxCena);
        return new ResponseEntity<>(dostupneSobe, HttpStatus.OK);
    }
}
