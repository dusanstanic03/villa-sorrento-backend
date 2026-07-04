package com.dusan.villa_sorrento_backend.controller;

import com.dusan.villa_sorrento_backend.dto.UslugaDTO;
import com.dusan.villa_sorrento_backend.service.UslugaService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST kontroler za upravljanje dodatnim uslugama.
 */
@RestController
@RequestMapping("/api/usluge")
public class UslugaController {
    private final UslugaService uslugaService;

    public UslugaController(UslugaService uslugaService) {
        this.uslugaService = uslugaService;
    }

    @PostMapping
    public ResponseEntity<UslugaDTO> createUsluga(@RequestBody UslugaDTO uslugaDTO) {
        try {
            return new ResponseEntity<>(uslugaService.createUsluga(uslugaDTO), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<List<UslugaDTO>> getAllUsluge() {
        return new ResponseEntity<>(uslugaService.getAllUsluge(), HttpStatus.OK);
    }

    @GetMapping("/aktivne")
    public ResponseEntity<List<UslugaDTO>> getAktivneUsluge() {
        return new ResponseEntity<>(uslugaService.getAktivneUsluge(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UslugaDTO> getUslugaById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(uslugaService.getUslugaById(id), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UslugaDTO> updateUsluga(@PathVariable Long id, @RequestBody UslugaDTO uslugaDTO) {
        try {
            return new ResponseEntity<>(uslugaService.updateUsluga(id, uslugaDTO), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsluga(@PathVariable Long id) {
        try {
            uslugaService.deleteUsluga(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
