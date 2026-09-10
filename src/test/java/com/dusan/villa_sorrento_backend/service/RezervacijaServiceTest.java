package com.dusan.villa_sorrento_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dusan.villa_sorrento_backend.dto.RezervacijaDTO;
import com.dusan.villa_sorrento_backend.dto.PlacanjeDTO;
import com.dusan.villa_sorrento_backend.dto.StavkaRezervacijeDTO;
import com.dusan.villa_sorrento_backend.mapper.RezervacijaMapper;
import com.dusan.villa_sorrento_backend.mapper.StavkaRezervacijeMapper;
import com.dusan.villa_sorrento_backend.model.Gost;
import com.dusan.villa_sorrento_backend.model.Rezervacija;
import com.dusan.villa_sorrento_backend.model.Soba;
import com.dusan.villa_sorrento_backend.model.StavkaRezervacije;
import com.dusan.villa_sorrento_backend.model.Usluga;
import com.dusan.villa_sorrento_backend.model.User;
import com.dusan.villa_sorrento_backend.repository.GostRepository;
import com.dusan.villa_sorrento_backend.repository.RezervacijaRepository;
import com.dusan.villa_sorrento_backend.repository.SobaRepository;
import com.dusan.villa_sorrento_backend.repository.StavkaRezervacijeRepository;
import com.dusan.villa_sorrento_backend.repository.UslugaRepository;
import com.dusan.villa_sorrento_backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RezervacijaServiceTest {

    @Mock
    private RezervacijaRepository rezervacijaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SobaRepository sobaRepository;

    @Mock
    private StavkaRezervacijeRepository stavkaRezervacijeRepository;

    @Mock
    private RezervacijaMapper rezervacijaMapper;

    @Mock
    private StavkaRezervacijeMapper stavkaRezervacijeMapper;

    @Mock
    private PlacanjeService placanjeService;

    @Mock
    private GostRepository gostRepository;

    @Mock
    private UslugaRepository uslugaRepository;

    @InjectMocks
    private RezervacijaService rezervacijaService;

    @Test
    void testCreateRezervacijaCalculatesRoomAndServiceAmount() {
        User user = new User();
        Soba soba = new Soba();
        soba.setIdSoba(1L);
        soba.setCena(100.0);
        soba.setOpis("Deluxe soba");
        Gost gost = new Gost();
        gost.setIdGost(2L);
        Usluga usluga = new Usluga();
        usluga.setIdUsluga(3L);
        usluga.setCena(25.0);
        StavkaRezervacijeDTO stavkaDTO = new StavkaRezervacijeDTO(null, 0,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 4), null, 1L,
                Set.of(2L), Set.of(3L), null, null);
        StavkaRezervacije stavka = new StavkaRezervacije();
        stavka.setDatumOd(stavkaDTO.getDatumOd());
        stavka.setDatumDo(stavkaDTO.getDatumDo());
        RezervacijaDTO output = new RezervacijaDTO(1L, 325.0, LocalDate.now(), 1L, Set.of());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sobaRepository.findById(1L)).thenReturn(Optional.of(soba));
        when(stavkaRezervacijeRepository.findOverlappingReservations(1L, stavkaDTO.getDatumOd(), stavkaDTO.getDatumDo())).thenReturn(List.of());
        when(stavkaRezervacijeMapper.stavkaRezervacijeDTOToStavkaRezervacije(stavkaDTO)).thenReturn(stavka);
        when(gostRepository.findById(2L)).thenReturn(Optional.of(gost));
        when(uslugaRepository.findById(3L)).thenReturn(Optional.of(usluga));
        when(rezervacijaRepository.save(any(Rezervacija.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(rezervacijaMapper.rezervacijaToRezervacijaDTO(any(Rezervacija.class))).thenReturn(output);

        RezervacijaDTO result = rezervacijaService.createRezervacija(1L, Set.of(stavkaDTO));

        ArgumentCaptor<Rezervacija> captor = ArgumentCaptor.forClass(Rezervacija.class);
        verify(rezervacijaRepository).save(captor.capture());
        Rezervacija saved = captor.getValue();

        assertEquals(325.0, saved.getIznos());
        assertEquals(1, saved.getStavkeRezervacije().size());
        assertEquals(325.0, saved.getStavkeRezervacije().iterator().next().getIznos());
        assertTrue(saved.getStavkeRezervacije().iterator().next().getGosti().contains(gost));
        assertTrue(saved.getStavkeRezervacije().iterator().next().getUsluge().contains(usluga));
        assertEquals(output, result);
    }

    @Test
    void testCreateRezervacijaThrowsWhenRoomIsOccupied() {
        User user = new User();
        Soba soba = new Soba();
        soba.setIdSoba(1L);
        soba.setOpis("Deluxe soba");
        StavkaRezervacijeDTO stavkaDTO = new StavkaRezervacijeDTO(null, 0,
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 4), null, 1L,
                Set.of(), Set.of(), null, null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sobaRepository.findById(1L)).thenReturn(Optional.of(soba));
        when(stavkaRezervacijeRepository.findOverlappingReservations(1L, stavkaDTO.getDatumOd(), stavkaDTO.getDatumDo()))
                .thenReturn(List.of(new StavkaRezervacije()));

        assertThrows(IllegalArgumentException.class, () -> rezervacijaService.createRezervacija(1L, Set.of(stavkaDTO)));
    }

    @Test
    void testCreateRezervacijaThrowsWhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rezervacijaService.createRezervacija(1L, new HashSet<>()));
    }

    @Test
    void testCreateRezervacijaThrowsWhenSobaDoesNotExist() {
        User user = new User();
        StavkaRezervacijeDTO stavkaDTO = new StavkaRezervacijeDTO(
                null,
                0,
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 4),
                null,
                99L,
                Set.of(),
                Set.of(),
                null,
                null
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sobaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> rezervacijaService.createRezervacija(1L, Set.of(stavkaDTO)));
    }

    @Test
    void testCreateRezervacijaThrowsWhenGostDoesNotExist() {
        User user = new User();
        Soba soba = new Soba();
        soba.setIdSoba(1L);
        soba.setCena(100.0);
        soba.setOpis("Deluxe soba");

        StavkaRezervacijeDTO stavkaDTO = new StavkaRezervacijeDTO(
                null,
                0,
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 4),
                null,
                1L,
                Set.of(99L),
                Set.of(),
                null,
                null
        );

        StavkaRezervacije stavka = new StavkaRezervacije();
        stavka.setDatumOd(stavkaDTO.getDatumOd());
        stavka.setDatumDo(stavkaDTO.getDatumDo());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sobaRepository.findById(1L)).thenReturn(Optional.of(soba));
        when(stavkaRezervacijeRepository.findOverlappingReservations(1L, stavkaDTO.getDatumOd(), stavkaDTO.getDatumDo()))
                .thenReturn(List.of());
        when(stavkaRezervacijeMapper.stavkaRezervacijeDTOToStavkaRezervacije(stavkaDTO)).thenReturn(stavka);
        when(gostRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> rezervacijaService.createRezervacija(1L, Set.of(stavkaDTO)));
    }

    @Test
    void testCreateRezervacijaThrowsWhenUslugaDoesNotExist() {
        User user = new User();
        Soba soba = new Soba();
        soba.setIdSoba(1L);
        soba.setCena(100.0);
        soba.setOpis("Deluxe soba");

        StavkaRezervacijeDTO stavkaDTO = new StavkaRezervacijeDTO(
                null,
                0,
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 4),
                null,
                1L,
                Set.of(),
                Set.of(99L),
                null,
                null
        );

        StavkaRezervacije stavka = new StavkaRezervacije();
        stavka.setDatumOd(stavkaDTO.getDatumOd());
        stavka.setDatumDo(stavkaDTO.getDatumDo());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sobaRepository.findById(1L)).thenReturn(Optional.of(soba));
        when(stavkaRezervacijeRepository.findOverlappingReservations(1L, stavkaDTO.getDatumOd(), stavkaDTO.getDatumDo()))
                .thenReturn(List.of());
        when(stavkaRezervacijeMapper.stavkaRezervacijeDTOToStavkaRezervacije(stavkaDTO)).thenReturn(stavka);
        when(uslugaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> rezervacijaService.createRezervacija(1L, Set.of(stavkaDTO)));
    }

    @Test
    void testCancelRezervacijaDeletesExistingReservation() {
        Rezervacija rezervacija = new Rezervacija();
        when(rezervacijaRepository.findById(1L)).thenReturn(Optional.of(rezervacija));

        rezervacijaService.cancelRezervacija(1L);

        verify(rezervacijaRepository).delete(rezervacija);
    }

    @Test
    void testGetAllRezervacijeReturnsDtos() {
        Rezervacija rezervacija = new Rezervacija();
        RezervacijaDTO dto = new RezervacijaDTO(1L, 100.0, LocalDate.now(), 1L, Set.of());
        when(rezervacijaRepository.findAll()).thenReturn(List.of(rezervacija));
        when(rezervacijaMapper.rezervacijaToRezervacijaDTO(rezervacija)).thenReturn(dto);

        assertEquals(List.of(dto), rezervacijaService.getAllRezervacije());
    }

    @Test
    void testGetRezervacijeByUserIdReturnsDtos() {
        Rezervacija rezervacija = new Rezervacija();
        RezervacijaDTO dto = new RezervacijaDTO(1L, 100.0, LocalDate.now(), 1L, Set.of());
        when(rezervacijaRepository.findByUserIdUser(1L)).thenReturn(List.of(rezervacija));
        when(rezervacijaMapper.rezervacijaToRezervacijaDTO(rezervacija)).thenReturn(dto);

        assertEquals(List.of(dto), rezervacijaService.getRezervacijeByUserId(1L));
    }

    @Test
    void testGetRezervacijaByIdThrowsWhenRezervacijaDoesNotExist() {
        when(rezervacijaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rezervacijaService.getRezervacijaById(99L));
    }

    @Test
    void testCancelRezervacijaThrowsWhenRezervacijaDoesNotExist() {
        when(rezervacijaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rezervacijaService.cancelRezervacija(99L));
    }

    @Test
    void testAddPaymentToRezervacijaDelegatesToPlacanjeService() {
        PlacanjeDTO input = new PlacanjeDTO(null, "COMPLETED", "CARD", 100.0, null, 1L);
        PlacanjeDTO output = new PlacanjeDTO(1L, "COMPLETED", "CARD", 100.0, null, 1L);
        when(placanjeService.createPlacanje(1L, input)).thenReturn(output);

        assertEquals(output, rezervacijaService.addPaymentToRezervacija(1L, input));
    }
}
