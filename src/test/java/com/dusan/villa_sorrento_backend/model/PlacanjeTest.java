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

class PlacanjeTest {

    private final Validator validator;

    PlacanjeTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Placanje validPlacanje() {
        Placanje placanje = new Placanje();
        placanje.setIdPlacanje(1L);
        placanje.setStatus("COMPLETED");
        placanje.setNacinPlacanja("CARD");
        placanje.setIznos(100.0);
        placanje.setDatumPlacanja(LocalDate.of(2026, 7, 5));
        placanje.setRezervacija(validRezervacija());
        return placanje;
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

    private void assertInvalid(Placanje placanje) {
        Set<ConstraintViolation<Placanje>> violations = validator.validate(placanje);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSetIdPlacanje() {
        Placanje placanje = validPlacanje();
        placanje.setIdPlacanje(2L);
        assertEquals(2L, placanje.getIdPlacanje());
    }

    @Test
    void testSetStatus() {
        Placanje placanje = validPlacanje();
        placanje.setStatus("PENDING");
        assertEquals("PENDING", placanje.getStatus());
    }

    @Test
    void testSetNacinPlacanja() {
        Placanje placanje = validPlacanje();
        placanje.setNacinPlacanja("CASH");
        assertEquals("CASH", placanje.getNacinPlacanja());
    }

    @Test
    void testSetIznos() {
        Placanje placanje = validPlacanje();
        placanje.setIznos(250.0);
        assertEquals(250.0, placanje.getIznos());
    }

    @Test
    void testSetDatumPlacanja() {
        Placanje placanje = validPlacanje();
        LocalDate datum = LocalDate.of(2026, 8, 1);
        placanje.setDatumPlacanja(datum);
        assertEquals(datum, placanje.getDatumPlacanja());
    }

    @Test
    void testSetRezervacija() {
        Placanje placanje = validPlacanje();
        Rezervacija rezervacija = validRezervacija();
        rezervacija.setIdRezervacija(2L);
        placanje.setRezervacija(rezervacija);
        assertEquals(rezervacija, placanje.getRezervacija());
    }

    @Test
    void testValidationPassesForValidPlacanje() {
        Placanje placanje = validPlacanje();
        Set<ConstraintViolation<Placanje>> violations = validator.validate(placanje);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenStatusIsBlank() {
        Placanje placanje = validPlacanje();
        placanje.setStatus("");
        assertInvalid(placanje);
    }

    @Test
    void testValidationFailsWhenStatusIsInvalid() {
        Placanje placanje = validPlacanje();
        placanje.setStatus("INVALID");
        assertInvalid(placanje);
    }

    @Test
    void testValidationFailsWhenNacinPlacanjaIsBlank() {
        Placanje placanje = validPlacanje();
        placanje.setNacinPlacanja("");
        assertInvalid(placanje);
    }

    @Test
    void testValidationFailsWhenNacinPlacanjaIsInvalid() {
        Placanje placanje = validPlacanje();
        placanje.setNacinPlacanja("TRANSFER");
        assertInvalid(placanje);
    }

    @Test
    void testValidationFailsWhenIznosIsNull() {
        Placanje placanje = validPlacanje();
        placanje.setIznos(null);
        assertInvalid(placanje);
    }

    @Test
    void testValidationFailsWhenIznosIsZero() {
        Placanje placanje = validPlacanje();
        placanje.setIznos(0.0);
        assertInvalid(placanje);
    }

    @Test
    void testValidationFailsWhenIznosIsNegative() {
        Placanje placanje = validPlacanje();
        placanje.setIznos(-1.0);
        assertInvalid(placanje);
    }

    @Test
    void testValidationFailsWhenDatumPlacanjaIsNull() {
        Placanje placanje = validPlacanje();
        placanje.setDatumPlacanja(null);
        assertInvalid(placanje);
    }

    @Test
    void testValidationFailsWhenRezervacijaIsNull() {
        Placanje placanje = validPlacanje();
        placanje.setRezervacija(null);
        assertInvalid(placanje);
    }

    @Test
    void testEqualsAndHashCode() {
        Placanje placanje1 = validPlacanje();
        Placanje placanje2 = validPlacanje();
        assertEquals(placanje1, placanje2);
        assertEquals(placanje1.hashCode(), placanje2.hashCode());
    }

    @Test
    void testToStringContainsBasicFieldsAndExcludesRelations() {
        Placanje placanje = validPlacanje();
        String result = placanje.toString();
        assertTrue(result.contains("idPlacanje=1"));
        assertTrue(result.contains("status=COMPLETED"));
        assertTrue(result.contains("nacinPlacanja=CARD"));
        assertTrue(result.contains("iznos=100.0"));
        assertTrue(result.contains("datumPlacanja=2026-07-05"));
        assertFalse(result.contains("rezervacija"));
    }
}