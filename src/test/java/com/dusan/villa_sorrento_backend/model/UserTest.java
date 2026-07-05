package com.dusan.villa_sorrento_backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void testUserStoresBasicDataAndReservations() {
        User user = new User();
        Rezervacija rezervacija = new Rezervacija();

        user.setIdUser(1L);
        user.setUsername("klijent1");
        user.setPassword("lozinka");
        user.setUloga("klijent");
        user.setBrojIsprave("ABC123");
        user.setBrojTelefona("060123456");
        user.setRezervacije(new HashSet<>());
        user.getRezervacije().add(rezervacija);

        assertEquals(1L, user.getIdUser());
        assertEquals("klijent1", user.getUsername());
        assertEquals("lozinka", user.getPassword());
        assertEquals("klijent", user.getUloga());
        assertEquals("ABC123", user.getBrojIsprave());
        assertEquals("060123456", user.getBrojTelefona());
        assertTrue(user.getRezervacije().contains(rezervacija));
    }
}
