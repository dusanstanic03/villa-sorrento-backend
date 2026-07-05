package com.dusan.villa_sorrento_backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PlacanjeTest {

    @Test
    void testPlacanjeStoresPaymentDataAndReservation() {
        Placanje placanje = new Placanje();
        Rezervacija rezervacija = new Rezervacija();

        placanje.setIdPlacanje(1L);
        placanje.setStatus("COMPLETED");
        placanje.setNacinPlacanja("CARD");
        placanje.setIznos(120.0);
        placanje.setDatumPlacanja(LocalDate.of(2026, 7, 5));
        placanje.setRezervacija(rezervacija);

        assertEquals(1L, placanje.getIdPlacanje());
        assertEquals("COMPLETED", placanje.getStatus());
        assertEquals("CARD", placanje.getNacinPlacanja());
        assertEquals(120.0, placanje.getIznos());
        assertEquals(LocalDate.of(2026, 7, 5), placanje.getDatumPlacanja());
        assertEquals(rezervacija, placanje.getRezervacija());
    }
}
