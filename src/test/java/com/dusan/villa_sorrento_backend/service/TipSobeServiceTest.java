package com.dusan.villa_sorrento_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dusan.villa_sorrento_backend.dto.TipSobeDTO;
import com.dusan.villa_sorrento_backend.mapper.TipSobeMapper;
import com.dusan.villa_sorrento_backend.model.TipSobe;
import com.dusan.villa_sorrento_backend.repository.TipSobeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TipSobeServiceTest {

    @Mock
    private TipSobeRepository tipSobeRepository;

    @Mock
    private TipSobeMapper tipSobeMapper;

    @InjectMocks
    private TipSobeService tipSobeService;

    @Test
    void testCreateTipSobeSavesNewType() {
        TipSobeDTO dto = new TipSobeDTO(null, "deluxe", "Deluxe soba", 2);
        TipSobe entity = new TipSobe();
        TipSobe saved = new TipSobe();
        TipSobeDTO savedDto = new TipSobeDTO(1L, "deluxe", "Deluxe soba", 2);

        when(tipSobeRepository.findByNaziv("deluxe")).thenReturn(Optional.empty());
        when(tipSobeMapper.tipSobeDTOToTipSobe(dto)).thenReturn(entity);
        when(tipSobeRepository.save(entity)).thenReturn(saved);
        when(tipSobeMapper.tipSobeToTipSobeDTO(saved)).thenReturn(savedDto);

        assertEquals(savedDto, tipSobeService.createTipSobe(dto));
    }

    @Test
    void testCreateTipSobeThrowsWhenNameExists() {
        TipSobeDTO dto = new TipSobeDTO(null, "deluxe", "Deluxe soba", 2);
        when(tipSobeRepository.findByNaziv("deluxe")).thenReturn(Optional.of(new TipSobe()));

        assertThrows(IllegalArgumentException.class, () -> tipSobeService.createTipSobe(dto));
        verify(tipSobeRepository, never()).save(any(TipSobe.class));
    }

    @Test
    void testGetAllTipoviSobaReturnsDtos() {
        TipSobe tip = new TipSobe();
        TipSobeDTO dto = new TipSobeDTO(1L, "tip", "opis", 2);
        when(tipSobeRepository.findAll()).thenReturn(List.of(tip));
        when(tipSobeMapper.tipSobeToTipSobeDTO(tip)).thenReturn(dto);

        assertEquals(List.of(dto), tipSobeService.getAllTipoviSoba());
    }

    @Test
    void testUpdateTipSobeThrowsWhenMissing() {
        when(tipSobeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> tipSobeService.updateTipSobe(1L, new TipSobeDTO()));
    }

    @Test
    void testGetTipSobeByIdReturnsDtoWhenExists() {
        TipSobe tip = new TipSobe();
        TipSobeDTO dto = new TipSobeDTO(1L, "tip", "opis", 2);
        when(tipSobeRepository.findById(1L)).thenReturn(Optional.of(tip));
        when(tipSobeMapper.tipSobeToTipSobeDTO(tip)).thenReturn(dto);

        assertEquals(dto, tipSobeService.getTipSobeById(1L));
    }

    @Test
    void testDeleteTipSobeDeletesExistingType() {
        when(tipSobeRepository.existsById(1L)).thenReturn(true);

        tipSobeService.deleteTipSobe(1L);

        verify(tipSobeRepository).deleteById(1L);
    }
}
