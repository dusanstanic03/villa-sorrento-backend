package com.dusan.villa_sorrento_backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RezervacijaTest {

    private final Validator validator;

    RezervacijaTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Rezervacija validRezervacija() {
        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setIdRezervacija(1L);
        rezervacija.setIznos(100.0);
        rezervacija.setDatumKreiranja(LocalDate.of(2026, 7, 1));
        rezervacija.setUser(validUser());
        rezervacija.setPlacanja(new HashSet<>());
        rezervacija.setStavkeRezervacije(new HashSet<>());
        return rezervacija;
    }

    private User validUser() {
        User user = new User();
        user.setIdUser(1L);
        user.setUsername("klijent1");
        user.setPassword("1234");
        user.setUloga("klijent");
        user.setBrojIsprave("12345");
        user.setBrojTelefona("060123456");
        user.setRezervacije(new HashSet<>());
        return user;
    }

    private void assertInvalid(Rezervacija rezervacija) {
        Set<ConstraintViolation<Rezervacija>> violations = validator.validate(rezervacija);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSetIdRezervacija() {
        Rezervacija rezervacija = validRezervacija();
        rezervacija.setIdRezervacija(2L);
        assertEquals(2L, rezervacija.getIdRezervacija());
    }

    @Test
    void testSetIznos() {
        Rezervacija rezervacija = validRezervacija();
        rezervacija.setIznos(250.0);
        assertEquals(250.0, rezervacija.getIznos());
    }

    @Test
    void testSetDatumKreiranja() {
        Rezervacija rezervacija = validRezervacija();
        LocalDate datum = LocalDate.of(2026, 8, 1);
        rezervacija.setDatumKreiranja(datum);
        assertEquals(datum, rezervacija.getDatumKreiranja());
    }

    @Test
    void testSetUser() {
        Rezervacija rezervacija = validRezervacija();
        User user = validUser();
        user.setIdUser(2L);
        rezervacija.setUser(user);
        assertEquals(user, rezervacija.getUser());
    }

    @Test
    void testSetPlacanja() {
        Rezervacija rezervacija = validRezervacija();
        Set<Placanje> placanja = new HashSet<>();
        placanja.add(new Placanje());
        rezervacija.setPlacanja(placanja);
        assertEquals(placanja, rezervacija.getPlacanja());
    }

    @Test
    void testSetStavkeRezervacije() {
        Rezervacija rezervacija = validRezervacija();
        Set<StavkaRezervacije> stavke = new HashSet<>();
        stavke.add(new StavkaRezervacije());
        rezervacija.setStavkeRezervacije(stavke);
        assertEquals(stavke, rezervacija.getStavkeRezervacije());
    }

    @Test
    void testValidationPassesForValidRezervacija() {
        Rezervacija rezervacija = validRezervacija();
        Set<ConstraintViolation<Rezervacija>> violations = validator.validate(rezervacija);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenIznosIsNegative() {
        Rezervacija rezervacija = validRezervacija();
        rezervacija.setIznos(-1.0);
        assertInvalid(rezervacija);
    }

    @Test
    void testValidationFailsWhenDatumKreiranjaIsNull() {
        Rezervacija rezervacija = validRezervacija();
        rezervacija.setDatumKreiranja(null);
        assertInvalid(rezervacija);
    }

    @Test
    void testValidationFailsWhenUserIsNull() {
        Rezervacija rezervacija = validRezervacija();
        rezervacija.setUser(null);
        assertInvalid(rezervacija);
    }

    @Test
    void testEqualsAndHashCode() {
        Rezervacija rezervacija1 = validRezervacija();
        Rezervacija rezervacija2 = validRezervacija();
        assertEquals(rezervacija1, rezervacija2);
        assertEquals(rezervacija1.hashCode(), rezervacija2.hashCode());
    }

    @Test
    void testToStringContainsBasicFieldsAndExcludesRelations() {
        Rezervacija rezervacija = validRezervacija();
        rezervacija.getPlacanja().add(new Placanje());
        rezervacija.getStavkeRezervacije().add(new StavkaRezervacije());
        String result = rezervacija.toString();
        assertTrue(result.contains("idRezervacija=1"));
        assertTrue(result.contains("iznos=100.0"));
        assertTrue(result.contains("datumKreiranja=2026-07-01"));
        assertFalse(result.contains("user"));
        assertFalse(result.contains("placanja"));
        assertFalse(result.contains("stavkeRezervacije"));
    }
}