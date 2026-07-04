package com.dusan.villa_sorrento_backend.controller;

import com.dusan.villa_sorrento_backend.dto.GostDTO;
import com.dusan.villa_sorrento_backend.service.GostService;
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
 * REST kontroler za upravljanje gostima.
 */
@RestController
@RequestMapping("/api/gosti")
public class GostController {
    private final GostService gostService;

    public GostController(GostService gostService) {
        this.gostService = gostService;
    }

    @PostMapping
    public ResponseEntity<GostDTO> createGost(@RequestBody GostDTO gostDTO) {
        try {
            return new ResponseEntity<>(gostService.createGost(gostDTO), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping
    public ResponseEntity<List<GostDTO>> getAllGosti() {
        return new ResponseEntity<>(gostService.getAllGosti(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GostDTO> getGostById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(gostService.getGostById(id), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GostDTO> updateGost(@PathVariable Long id, @RequestBody GostDTO gostDTO) {
        try {
            return new ResponseEntity<>(gostService.updateGost(id, gostDTO), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGost(@PathVariable Long id) {
        try {
            gostService.deleteGost(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
