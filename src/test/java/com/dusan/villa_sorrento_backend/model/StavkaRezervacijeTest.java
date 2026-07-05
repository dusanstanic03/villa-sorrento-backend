package com.dusan.villa_sorrento_backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

class StavkaRezervacijeTest {

    @Test
    void testStavkaStoresRoomGuestsAndServices() {
        StavkaRezervacije stavka = new StavkaRezervacije();
        Rezervacija rezervacija = new Rezervacija();
        Soba soba = new Soba();
        Gost gost = new Gost();
        Usluga usluga = new Usluga();

        stavka.setId(1L);
        stavka.setRb(1);
        stavka.setDatumOd(LocalDate.of(2026, 8, 1));
        stavka.setDatumDo(LocalDate.of(2026, 8, 5));
        stavka.setIznos(500.0);
        stavka.setRezervacija(rezervacija);
        stavka.setSoba(soba);
        stavka.setGosti(new HashSet<>());
        stavka.setUsluge(new HashSet<>());
        stavka.getGosti().add(gost);
        stavka.getUsluge().add(usluga);

        assertEquals(1L, stavka.getId());
        assertEquals(1, stavka.getRb());
        assertEquals(LocalDate.of(2026, 8, 1), stavka.getDatumOd());
        assertEquals(LocalDate.of(2026, 8, 5), stavka.getDatumDo());
        assertEquals(500.0, stavka.getIznos());
        assertEquals(rezervacija, stavka.getRezervacija());
        assertEquals(soba, stavka.getSoba());
        assertTrue(stavka.getGosti().contains(gost));
        assertTrue(stavka.getUsluge().contains(usluga));
    }
}
