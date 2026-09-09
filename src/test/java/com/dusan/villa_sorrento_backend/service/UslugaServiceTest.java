package com.dusan.villa_sorrento_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dusan.villa_sorrento_backend.dto.UslugaDTO;
import com.dusan.villa_sorrento_backend.mapper.UslugaMapper;
import com.dusan.villa_sorrento_backend.model.Usluga;
import com.dusan.villa_sorrento_backend.repository.UslugaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UslugaServiceTest {

    @Mock
    private UslugaRepository uslugaRepository;

    @Mock
    private UslugaMapper uslugaMapper;

    @InjectMocks
    private UslugaService uslugaService;

    @Test
    void testCreateUslugaSetsDefaultActiveWhenMissing() {
        UslugaDTO dto = new UslugaDTO(null, "Parking", "Parking mesto", 8.0, null);
        Usluga entity = new Usluga();
        Usluga saved = new Usluga();
        UslugaDTO savedDto = new UslugaDTO(1L, "Parking", "Parking mesto", 8.0, true);

        when(uslugaRepository.findByNaziv("Parking")).thenReturn(Optional.empty());
        when(uslugaMapper.uslugaDTOToUsluga(dto)).thenReturn(entity);
        when(uslugaRepository.save(entity)).thenReturn(saved);
        when(uslugaMapper.uslugaToUslugaDTO(saved)).thenReturn(savedDto);

        UslugaDTO result = uslugaService.createUsluga(dto);

        assertTrue(dto.getAktivna());
        assertEquals(savedDto, result);
    }

    @Test
    void testGetAktivneUslugeReturnsOnlyActiveServicesFromRepository() {
        Usluga usluga = new Usluga();
        UslugaDTO dto = new UslugaDTO(1L, "Dorucak", "Opis", 12.0, true);
        when(uslugaRepository.findByAktivna(true)).thenReturn(List.of(usluga));
        when(uslugaMapper.uslugaToUslugaDTO(usluga)).thenReturn(dto);

        assertEquals(List.of(dto), uslugaService.getAktivneUsluge());
    }

    @Test
    void testGetAllUslugeReturnsDtos() {
        Usluga usluga = new Usluga();
        UslugaDTO dto = new UslugaDTO(1L, "Parking", "Opis", 8.0, true);

        when(uslugaRepository.findAll()).thenReturn(List.of(usluga));
        when(uslugaMapper.uslugaToUslugaDTO(usluga)).thenReturn(dto);

        assertEquals(List.of(dto), uslugaService.getAllUsluge());
    }

    @Test
    void testGetUslugaByIdThrowsWhenMissing() {
        when(uslugaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> uslugaService.getUslugaById(1L));
    }

    @Test
    void testCreateUslugaThrowsWhenNameExists() {
        UslugaDTO dto = new UslugaDTO(null, "Parking", "Parking mesto", 8.0, true);
        when(uslugaRepository.findByNaziv("Parking")).thenReturn(Optional.of(new Usluga()));

        assertThrows(IllegalArgumentException.class, () -> uslugaService.createUsluga(dto));
        verify(uslugaRepository, never()).save(any(Usluga.class));
    }

    @Test
    void testUpdateUslugaUpdatesExistingService() {
        Usluga usluga = new Usluga();
        UslugaDTO dto = new UslugaDTO(1L, "Spa", "Opis", 30.0, true);
        UslugaDTO output = new UslugaDTO(1L, "Spa", "Opis", 30.0, true);
        when(uslugaRepository.findById(1L)).thenReturn(Optional.of(usluga));
        when(uslugaRepository.save(usluga)).thenReturn(usluga);
        when(uslugaMapper.uslugaToUslugaDTO(usluga)).thenReturn(output);

        assertEquals(output, uslugaService.updateUsluga(1L, dto));
        verify(uslugaMapper).updateUslugaFromDto(dto, usluga);
    }

    @Test
    void testUpdateUslugaThrowsWhenUslugaDoesNotExist() {
        UslugaDTO dto = new UslugaDTO(99L, "Spa", "Opis", 30.0, true);
        when(uslugaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> uslugaService.updateUsluga(99L, dto));

        verify(uslugaRepository, never()).save(any(Usluga.class));
    }

    @Test
    void testDeleteUslugaDeletesExistingService() {
        when(uslugaRepository.existsById(1L)).thenReturn(true);

        uslugaService.deleteUsluga(1L);

        verify(uslugaRepository).deleteById(1L);
    }

    @Test
    void testDeleteUslugaThrowsWhenUslugaDoesNotExist() {
        when(uslugaRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> uslugaService.deleteUsluga(99L));

        verify(uslugaRepository, never()).deleteById(99L);
    }
}
