package com.dusan.villa_sorrento_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.dusan.villa_sorrento_backend.dto.PlacanjeDTO;
import com.dusan.villa_sorrento_backend.mapper.PlacanjeMapper;
import com.dusan.villa_sorrento_backend.model.Placanje;
import com.dusan.villa_sorrento_backend.model.Rezervacija;
import com.dusan.villa_sorrento_backend.repository.PlacanjeRepository;
import com.dusan.villa_sorrento_backend.repository.RezervacijaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlacanjeServiceTest {

    @Mock
    private PlacanjeRepository placanjeRepository;

    @Mock
    private RezervacijaRepository rezervacijaRepository;

    @Mock
    private PlacanjeMapper placanjeMapper;

    @InjectMocks
    private PlacanjeService placanjeService;

    @Test
    void testCreatePlacanjeSetsReservationDateAndDefaultStatus() {
        Rezervacija rezervacija = new Rezervacija();
        PlacanjeDTO input = new PlacanjeDTO(null, null, "CASH", 100.0, null, null);
        Placanje entity = new Placanje();
        Placanje saved = new Placanje();
        PlacanjeDTO output = new PlacanjeDTO(1L, "PENDING", "CASH", 100.0, null, 1L);

        when(rezervacijaRepository.findById(1L)).thenReturn(Optional.of(rezervacija));
        when(placanjeMapper.placanjeDTOToPlacanje(input)).thenReturn(entity);
        when(placanjeRepository.save(entity)).thenReturn(saved);
        when(placanjeMapper.placanjeToPlacanjeDTO(saved)).thenReturn(output);

        PlacanjeDTO result = placanjeService.createPlacanje(1L, input);

        assertEquals(rezervacija, entity.getRezervacija());
        assertEquals("PENDING", entity.getStatus());
        assertEquals(output, result);
    }

    @Test
    void testUpdateStatusPlacanjaChangesStatus() {
        Placanje placanje = new Placanje();
        Placanje saved = new Placanje();
        PlacanjeDTO output = new PlacanjeDTO(1L, "COMPLETED", "CARD", 100.0, null, 1L);

        when(placanjeRepository.findById(1L)).thenReturn(Optional.of(placanje));
        when(placanjeRepository.save(placanje)).thenReturn(saved);
        when(placanjeMapper.placanjeToPlacanjeDTO(saved)).thenReturn(output);

        PlacanjeDTO result = placanjeService.updateStatusPlacanja(1L, "COMPLETED");

        assertEquals("COMPLETED", placanje.getStatus());
        assertEquals(output, result);
    }

    @Test
    void testCreatePlacanjeThrowsWhenReservationMissing() {
        when(rezervacijaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> placanjeService.createPlacanje(1L, new PlacanjeDTO()));
    }

    @Test
    void testCalculateTotalPaidForReservationSumsOnlyCompletedAndProcessed() {
        Placanje completed = new Placanje();
        completed.setStatus("COMPLETED");
        completed.setIznos(100.0);
        Placanje processed = new Placanje();
        processed.setStatus("PROCESSED");
        processed.setIznos(50.0);
        Placanje pending = new Placanje();
        pending.setStatus("PENDING");
        pending.setIznos(30.0);

        when(placanjeRepository.findByRezervacija_IdRezervacija(1L)).thenReturn(List.of(completed, processed, pending));

        assertEquals(150.0, placanjeService.calculateTotalPaidForReservation(1L));
    }

    @Test
    void testGetPlacanjaByRezervacijaIdReturnsDtos() {
        Placanje placanje = new Placanje();
        PlacanjeDTO dto = new PlacanjeDTO(1L, "COMPLETED", "CARD", 100.0, null, 1L);
        when(placanjeRepository.findByRezervacija_IdRezervacija(1L)).thenReturn(List.of(placanje));
        when(placanjeMapper.placanjeToPlacanjeDTO(placanje)).thenReturn(dto);

        assertEquals(List.of(dto), placanjeService.getPlacanjaByRezervacijaId(1L));
    }

    @Test
    void testGetAllPlacanjaReturnsDtos() {
        Placanje placanje = new Placanje();
        PlacanjeDTO dto = new PlacanjeDTO(1L, "COMPLETED", "CARD", 100.0, null, 1L);
        when(placanjeRepository.findAll()).thenReturn(List.of(placanje));
        when(placanjeMapper.placanjeToPlacanjeDTO(placanje)).thenReturn(dto);

        assertEquals(List.of(dto), placanjeService.getAllPlacanja());
    }

    @Test
    void testGetPlacanjeByIdThrowsWhenMissing() {
        when(placanjeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> placanjeService.getPlacanjeById(1L));
    }
}
