package com.dusan.villa_sorrento_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dusan.villa_sorrento_backend.dto.GostDTO;
import com.dusan.villa_sorrento_backend.mapper.GostMapper;
import com.dusan.villa_sorrento_backend.model.Gost;
import com.dusan.villa_sorrento_backend.repository.GostRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GostServiceTest {

    @Mock
    private GostRepository gostRepository;

    @Mock
    private GostMapper gostMapper;

    @InjectMocks
    private GostService gostService;

    @Test
    void testCreateGostSavesNewGuestWhenDocumentDoesNotExist() {
        GostDTO dto = new GostDTO(null, "Pera", "Peric", "12345", "060123456");
        Gost entity = new Gost();
        Gost saved = new Gost();
        GostDTO savedDto = new GostDTO(1L, "Pera", "Peric", "12345", "060123456");

        when(gostRepository.findByBrojIsprave("12345")).thenReturn(Optional.empty());
        when(gostMapper.gostDTOToGost(dto)).thenReturn(entity);
        when(gostRepository.save(entity)).thenReturn(saved);
        when(gostMapper.gostToGostDTO(saved)).thenReturn(savedDto);

        assertEquals(savedDto, gostService.createGost(dto));
    }

    @Test
    void testCreateGostReturnsExistingGuestWhenDocumentExists() {
        GostDTO dto = new GostDTO(null, "Pera", "Peric", "12345", "060123456");
        Gost existing = new Gost();
        GostDTO existingDto = new GostDTO(5L, "Pera", "Peric", "12345", "060123456");

        when(gostRepository.findByBrojIsprave("12345")).thenReturn(Optional.of(existing));
        when(gostMapper.gostToGostDTO(existing)).thenReturn(existingDto);

        assertEquals(existingDto, gostService.createGost(dto));
        verify(gostRepository, never()).save(any(Gost.class));
    }


    @Test
    void testGetAllGostiReturnsDtos() {
        Gost gost = new Gost();
        GostDTO dto = new GostDTO(1L, "Pera", "Peric", "12345", "060123456");
        when(gostRepository.findAll()).thenReturn(List.of(gost));
        when(gostMapper.gostToGostDTO(gost)).thenReturn(dto);

        assertEquals(List.of(dto), gostService.getAllGosti());
    }

    @Test
    void testGetGostByIdReturnsDtoWhenExists() {
        Gost gost = new Gost();
        GostDTO dto = new GostDTO(1L, "Pera", "Peric", "12345", "060123456");

        when(gostRepository.findById(1L)).thenReturn(Optional.of(gost));
        when(gostMapper.gostToGostDTO(gost)).thenReturn(dto);

        assertEquals(dto, gostService.getGostById(1L));
    }

    @Test
    void testGetGostByIdThrowsWhenGostDoesNotExist() {
        when(gostRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> gostService.getGostById(99L));
    }

    @Test
    void testUpdateGostUpdatesExistingGuest() {
        Gost gost = new Gost();
        GostDTO dto = new GostDTO(1L, "Novo", "Prezime", "12345", "060123456");
        GostDTO output = new GostDTO(1L, "Novo", "Prezime", "12345", "060123456");
        when(gostRepository.findById(1L)).thenReturn(Optional.of(gost));
        when(gostRepository.save(gost)).thenReturn(gost);
        when(gostMapper.gostToGostDTO(gost)).thenReturn(output);

        assertEquals(output, gostService.updateGost(1L, dto));
        verify(gostMapper).updateGostFromDto(dto, gost);
    }

    @Test
    void testUpdateGostThrowsWhenGostDoesNotExist() {
        GostDTO dto = new GostDTO(99L, "Pera", "Peric", "12345", "060123456");
        when(gostRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> gostService.updateGost(99L, dto));

        verify(gostRepository, never()).save(any(Gost.class));
    }

    @Test
    void testDeleteGostDeletesExistingGuest() {
        when(gostRepository.existsById(1L)).thenReturn(true);

        gostService.deleteGost(1L);

        verify(gostRepository).deleteById(1L);
    }

    @Test
    void testDeleteGostThrowsWhenMissing() {
        when(gostRepository.existsById(9L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> gostService.deleteGost(9L));
    }
}
