package com.dusan.villa_sorrento_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.dusan.villa_sorrento_backend.dto.SobaDTO;
import com.dusan.villa_sorrento_backend.mapper.SobaMapper;
import com.dusan.villa_sorrento_backend.model.Soba;
import com.dusan.villa_sorrento_backend.model.StavkaRezervacije;
import com.dusan.villa_sorrento_backend.model.TipSobe;
import com.dusan.villa_sorrento_backend.repository.SobaRepository;
import com.dusan.villa_sorrento_backend.repository.StavkaRezervacijeRepository;
import com.dusan.villa_sorrento_backend.repository.TipSobeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SobaServiceTest {

    @Mock
    private SobaRepository sobaRepository;

    @Mock
    private SobaMapper sobaMapper;

    @Mock
    private StavkaRezervacijeRepository stavkaRezervacijeRepository;

    @Mock
    private TipSobeRepository tipSobeRepository;

    @InjectMocks
    private SobaService sobaService;

    @Test
    void testCreateSobaUsesSelectedRoomType() {
        SobaDTO input = new SobaDTO(null, "Deluxe soba", 100.0, "noc", null, 1L, null, "slika.jpg");
        Soba entity = new Soba();
        Soba saved = new Soba();
        TipSobe tip = new TipSobe();
        SobaDTO output = new SobaDTO(10L, "Deluxe soba", 100.0, "noc", true, 1L, "dvokrevetna", "slika.jpg");

        when(sobaMapper.sobaDTOToSoba(input)).thenReturn(entity);
        when(tipSobeRepository.findById(1L)).thenReturn(Optional.of(tip));
        when(sobaRepository.save(entity)).thenReturn(saved);
        when(sobaMapper.sobaToSobaDTO(saved)).thenReturn(output);

        SobaDTO result = sobaService.createSoba(input);

        assertEquals(tip, entity.getTipSobe());
        assertEquals(true, entity.getDostupna());
        assertEquals(output, result);
    }

    @Test
    void testCreateSobaThrowsWhenRoomTypeIdDoesNotExist() {
        SobaDTO input = new SobaDTO(null, "Deluxe soba", 100.0, "noc", null, 99L, null, "slika.jpg");
        when(sobaMapper.sobaDTOToSoba(input)).thenReturn(new Soba());
        when(tipSobeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sobaService.createSoba(input));
    }

    @Test
    void testCreateSobaThrowsWhenRoomTypeNameDoesNotExist() {
        SobaDTO input = new SobaDTO(null, "Deluxe soba", 100.0, "noc", null, null, "nepoznat", "slika.jpg");

        when(sobaMapper.sobaDTOToSoba(input)).thenReturn(new Soba());
        when(tipSobeRepository.findByNaziv("nepoznat")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sobaService.createSoba(input));

        verify(sobaRepository, never()).save(any(Soba.class));
    }

    @Test
    void testCreateSobaThrowsWhenRoomTypeIsNotProvided() {
        SobaDTO input = new SobaDTO(null, "Deluxe soba", 100.0, "noc", null, null, null, "slika.jpg");

        when(sobaMapper.sobaDTOToSoba(input)).thenReturn(new Soba());

        assertThrows(IllegalArgumentException.class, () -> sobaService.createSoba(input));

        verify(sobaRepository, never()).save(any(Soba.class));
    }


    @Test
    void testDeleteSobaDeletesExistingRoom() {
        when(sobaRepository.existsById(1L)).thenReturn(true);

        sobaService.deleteSoba(1L);

        verify(sobaRepository).deleteById(1L);
    }

    @Test
    void testDeleteSobaThrowsWhenSobaDoesNotExist() {
        when(sobaRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> sobaService.deleteSoba(99L));

        verify(sobaRepository, never()).deleteById(99L);
    }

    @Test
    void testPretraziDostupneSobeExcludesOverlappingReservations() {
        TipSobe tip = new TipSobe();
        tip.setNaziv("deluxe");
        Soba slobodna = new Soba();
        slobodna.setIdSoba(1L);
        slobodna.setTipSobe(tip);
        slobodna.setCena(100.0);
        Soba zauzeta = new Soba();
        zauzeta.setIdSoba(2L);
        zauzeta.setTipSobe(tip);
        zauzeta.setCena(120.0);
        SobaDTO slobodnaDto = new SobaDTO(1L, "Slobodna", 100.0, "noc", true, 1L, "deluxe", "s.jpg");

        LocalDate od = LocalDate.of(2026, 8, 1);
        LocalDate doDatuma = LocalDate.of(2026, 8, 5);

        when(sobaRepository.findByDostupna(true)).thenReturn(List.of(slobodna, zauzeta));
        when(stavkaRezervacijeRepository.findOverlappingReservations(1L, od, doDatuma)).thenReturn(List.of());
        when(stavkaRezervacijeRepository.findOverlappingReservations(2L, od, doDatuma)).thenReturn(List.of(new StavkaRezervacije()));
        when(sobaMapper.sobaToSobaDTO(slobodna)).thenReturn(slobodnaDto);

        List<SobaDTO> result = sobaService.pretraziDostupneSobe(od, doDatuma, "deluxe", null, null);

        assertEquals(List.of(slobodnaDto), result);
    }

    @Test
    void testGetAllSobeReturnsDtos() {
        Soba soba = new Soba();
        SobaDTO dto = new SobaDTO(1L, "Deluxe soba", 100.0, "noc", true, 1L, "deluxe", "s.jpg");
        when(sobaRepository.findAll()).thenReturn(List.of(soba));
        when(sobaMapper.sobaToSobaDTO(soba)).thenReturn(dto);

        assertEquals(List.of(dto), sobaService.getAllSobe());
    }

    @Test
    void testGetSobaByIdReturnsDtoWhenExists() {
        Soba soba = new Soba();
        SobaDTO dto = new SobaDTO(1L, "Deluxe soba", 100.0, "noc", true, 1L, "deluxe", "s.jpg");
        when(sobaRepository.findById(1L)).thenReturn(Optional.of(soba));
        when(sobaMapper.sobaToSobaDTO(soba)).thenReturn(dto);

        assertEquals(dto, sobaService.getSobaById(1L));
    }

    @Test
    void testGetSobaByIdThrowsWhenSobaDoesNotExist() {
        when(sobaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sobaService.getSobaById(99L));
    }

    @Test
    void testUpdateSobaUpdatesExistingRoom() {
        Soba existing = new Soba();
        TipSobe tip = new TipSobe();
        SobaDTO input = new SobaDTO(1L, "Deluxe soba", 120.0, "noc", true, 2L, null, "s.jpg");
        SobaDTO output = new SobaDTO(1L, "Deluxe soba", 120.0, "noc", true, 2L, "deluxe", "s.jpg");

        when(sobaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(tipSobeRepository.findById(2L)).thenReturn(Optional.of(tip));
        when(sobaRepository.save(existing)).thenReturn(existing);
        when(sobaMapper.sobaToSobaDTO(existing)).thenReturn(output);

        assertEquals(output, sobaService.updateSoba(1L, input));
        assertEquals(tip, existing.getTipSobe());
        verify(sobaMapper).updateSobaFromDto(input, existing);
    }

    @Test
    void testUpdateSobaThrowsWhenSobaDoesNotExist() {
        SobaDTO dto = new SobaDTO(99L, "Deluxe soba", 100.0, "noc", true, 1L, null, "slika.jpg");
        when(sobaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sobaService.updateSoba(99L, dto));

        verify(sobaRepository, never()).save(any(Soba.class));
    }
}
