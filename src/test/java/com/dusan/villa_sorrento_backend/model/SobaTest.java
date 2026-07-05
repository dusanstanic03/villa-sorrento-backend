package com.dusan.villa_sorrento_backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import org.junit.jupiter.api.Test;

class SobaTest {

    @Test
    void testSobaStoresTypeAndReservationItems() {
        Soba soba = new Soba();
        TipSobe tipSobe = new TipSobe();
        StavkaRezervacije stavka = new StavkaRezervacije();

        soba.setIdSoba(10L);
        soba.setOpis("Dvokrevetna deluxe");
        soba.setCena(200.0);
        soba.setJedinicaMere("noc");
        soba.setDostupna(true);
        soba.setTipSobe(tipSobe);
        soba.setSlikaUrl("slika.jpg");
        soba.setStavkeRezervacije(new HashSet<>());
        soba.getStavkeRezervacije().add(stavka);

        assertEquals(10L, soba.getIdSoba());
        assertEquals("Dvokrevetna deluxe", soba.getOpis());
        assertEquals(200.0, soba.getCena());
        assertEquals("noc", soba.getJedinicaMere());
        assertEquals(true, soba.getDostupna());
        assertEquals(tipSobe, soba.getTipSobe());
        assertEquals("slika.jpg", soba.getSlikaUrl());
        assertTrue(soba.getStavkeRezervacije().contains(stavka));
    }
}
