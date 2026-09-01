package com.dusan.villa_sorrento_backend.controller;

import com.dusan.villa_sorrento_backend.dto.TipSobeDTO;
import com.dusan.villa_sorrento_backend.service.TipSobeService;
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
 * @author Dusan
 * REST kontroler za upravljanje tipovima soba.
 */
@RestController
@RequestMapping("/api/tipovi-soba")
public class TipSobeController {
    private final TipSobeService tipSobeService;

    public TipSobeController(TipSobeService tipSobeService) {
        this.tipSobeService = tipSobeService;
    }

    @PostMapping
    public ResponseEntity<TipSobeDTO> createTipSobe(@RequestBody TipSobeDTO tipSobeDTO) {
        try {
            return new ResponseEntity<>(tipSobeService.createTipSobe(tipSobeDTO), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<List<TipSobeDTO>> getAllTipoviSoba() {
        return new ResponseEntity<>(tipSobeService.getAllTipoviSoba(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipSobeDTO> getTipSobeById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(tipSobeService.getTipSobeById(id), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipSobeDTO> updateTipSobe(@PathVariable Long id, @RequestBody TipSobeDTO tipSobeDTO) {
        try {
            return new ResponseEntity<>(tipSobeService.updateTipSobe(id, tipSobeDTO), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipSobe(@PathVariable Long id) {
        try {
            tipSobeService.deleteTipSobe(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
