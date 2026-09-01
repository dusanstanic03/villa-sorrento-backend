package com.dusan.villa_sorrento_backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import org.junit.jupiter.api.Test;

class GostTest {

    @Test
    void testGostCanBeAssignedToMultipleReservationItems() {
        Gost gost = new Gost();
        StavkaRezervacije prvaStavka = new StavkaRezervacije();
        StavkaRezervacije drugaStavka = new StavkaRezervacije();
        prvaStavka.setId(1L);
        drugaStavka.setId(2L);

        gost.setIdGost(1L);
        gost.setIme("Petar");
        gost.setPrezime("Petrovic");
        gost.setBrojIsprave("P123");
        gost.setBrojTelefona("060111222");
        gost.setStavkeRezervacije(new HashSet<>());
        gost.getStavkeRezervacije().add(prvaStavka);
        gost.getStavkeRezervacije().add(drugaStavka);

        assertEquals("Petar", gost.getIme());
        assertEquals("Petrovic", gost.getPrezime());
        assertEquals("P123", gost.getBrojIsprave());
        assertEquals("060111222", gost.getBrojTelefona());
        assertEquals(2, gost.getStavkeRezervacije().size());
        assertTrue(gost.getStavkeRezervacije().contains(prvaStavka));
    }
}
