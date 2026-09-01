package com.dusan.villa_sorrento_backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import org.junit.jupiter.api.Test;

class TipSobeTest {

    @Test
    void testTipSobeStoresRoomsAndCapacity() {
        TipSobe tipSobe = new TipSobe();
        Soba soba = new Soba();

        tipSobe.setIdTipSobe(1L);
        tipSobe.setNaziv("dvokrevetna pm");
        tipSobe.setOpis("Dvokrevetna soba sa pogledom na more");
        tipSobe.setKapacitet(2);
        tipSobe.setSobe(new HashSet<>());
        tipSobe.getSobe().add(soba);

        assertEquals(1L, tipSobe.getIdTipSobe());
        assertEquals("dvokrevetna pm", tipSobe.getNaziv());
        assertEquals("Dvokrevetna soba sa pogledom na more", tipSobe.getOpis());
        assertEquals(2, tipSobe.getKapacitet());
        assertTrue(tipSobe.getSobe().contains(soba));
    }
}
