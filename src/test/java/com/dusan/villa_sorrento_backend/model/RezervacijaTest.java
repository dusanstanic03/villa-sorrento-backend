package com.dusan.villa_sorrento_backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

class RezervacijaTest {

    @Test
    void testRezervacijaStoresUserPaymentsAndItems() {
        Rezervacija rezervacija = new Rezervacija();
        User user = new User();
        Placanje placanje = new Placanje();
        StavkaRezervacije stavka = new StavkaRezervacije();

        rezervacija.setIdRezervacija(1L);
        rezervacija.setIznos(350.0);
        rezervacija.setDatumKreiranja(LocalDate.of(2026, 7, 5));
        rezervacija.setUser(user);
        rezervacija.setPlacanja(new HashSet<>());
        rezervacija.setStavkeRezervacije(new HashSet<>());
        rezervacija.getPlacanja().add(placanje);
        rezervacija.getStavkeRezervacije().add(stavka);

        assertEquals(1L, rezervacija.getIdRezervacija());
        assertEquals(350.0, rezervacija.getIznos());
        assertEquals(LocalDate.of(2026, 7, 5), rezervacija.getDatumKreiranja());
        assertEquals(user, rezervacija.getUser());
        assertTrue(rezervacija.getPlacanja().contains(placanje));
        assertTrue(rezervacija.getStavkeRezervacije().contains(stavka));
    }
}
