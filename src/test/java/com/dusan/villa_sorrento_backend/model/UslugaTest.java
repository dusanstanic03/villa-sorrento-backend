package com.dusan.villa_sorrento_backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import org.junit.jupiter.api.Test;

class UslugaTest {

    @Test
    void testUslugaStoresPriceStatusAndItems() {
        Usluga usluga = new Usluga();
        StavkaRezervacije stavka = new StavkaRezervacije();

        usluga.setIdUsluga(1L);
        usluga.setNaziv("Dorucak");
        usluga.setOpis("Dorucak za jednog gosta");
        usluga.setCena(12.0);
        usluga.setAktivna(true);
        usluga.setStavkeRezervacije(new HashSet<>());
        usluga.getStavkeRezervacije().add(stavka);

        assertEquals("Dorucak", usluga.getNaziv());
        assertEquals("Dorucak za jednog gosta", usluga.getOpis());
        assertEquals(12.0, usluga.getCena());
        assertTrue(usluga.getAktivna());
        assertTrue(usluga.getStavkeRezervacije().contains(stavka));

        usluga.setAktivna(false);
        assertFalse(usluga.getAktivna());
    }
}
