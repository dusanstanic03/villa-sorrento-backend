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
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlacanjeTest {

    private final Validator validator;
    private Placanje placanje;

    PlacanjeTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        placanje = validPlacanje();
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

    private void assertValidationViolations(String propertyName, String... expectedMessages) {
        Set<ConstraintViolation<Placanje>> violations = validator.validate(placanje);
        assertEquals(expectedMessages.length, violations.size());
        assertTrue(violations.stream()
                .allMatch(violation -> propertyName.equals(violation.getPropertyPath().toString())));

        Set<String> actualMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
        assertEquals(Set.of(expectedMessages), actualMessages);
    }

    @Test
    void testSetIdPlacanje() {
        placanje.setIdPlacanje(2L);
        assertEquals(2L, placanje.getIdPlacanje());
    }

    @Test
    void testSetStatus() {
        placanje.setStatus("PENDING");
        assertEquals("PENDING", placanje.getStatus());
    }

    @Test
    void testSetNacinPlacanja() {
        placanje.setNacinPlacanja("CASH");
        assertEquals("CASH", placanje.getNacinPlacanja());
    }

    @Test
    void testSetIznos() {
        placanje.setIznos(250.0);
        assertEquals(250.0, placanje.getIznos());
    }

    @Test
    void testSetDatumPlacanja() {
        LocalDate datum = LocalDate.of(2026, 8, 1);
        placanje.setDatumPlacanja(datum);
        assertEquals(datum, placanje.getDatumPlacanja());
    }

    @Test
    void testSetRezervacija() {
        Rezervacija rezervacija = validRezervacija();
        rezervacija.setIdRezervacija(2L);
        placanje.setRezervacija(rezervacija);
        assertEquals(rezervacija, placanje.getRezervacija());
    }

    @Test
    void testValidationPassesForValidPlacanje() {
        Set<ConstraintViolation<Placanje>> violations = validator.validate(placanje);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenStatusIsBlank() {
        placanje.setStatus("   ");
        assertValidationViolations("status",
                "Status placanja je obavezan.",
                "Status placanja nije dozvoljen.");
    }

    @Test
    void testValidationFailsWhenStatusIsInvalid() {
        placanje.setStatus("INVALID");
        assertValidationViolations("status", "Status placanja nije dozvoljen.");
    }

    @Test
    void testValidationFailsWhenNacinPlacanjaIsBlank() {
        placanje.setNacinPlacanja("   ");
        assertValidationViolations("nacinPlacanja",
                "Nacin placanja je obavezan.",
                "Nacin placanja mora biti CARD, CRYPTO ili CASH.");
    }

    @Test
    void testValidationFailsWhenNacinPlacanjaIsInvalid() {
        placanje.setNacinPlacanja("TRANSFER");
        assertValidationViolations("nacinPlacanja", "Nacin placanja mora biti CARD, CRYPTO ili CASH.");
    }

    @Test
    void testValidationFailsWhenIznosIsNull() {
        placanje.setIznos(null);
        assertValidationViolations("iznos", "Iznos placanja je obavezan.");
    }

    @Test
    void testValidationFailsWhenIznosIsZero() {
        placanje.setIznos(0.0);
        assertValidationViolations("iznos", "Iznos placanja mora biti pozitivan.");
    }

    @Test
    void testValidationFailsWhenIznosIsNegative() {
        placanje.setIznos(-1.0);
        assertValidationViolations("iznos", "Iznos placanja mora biti pozitivan.");
    }

    @Test
    void testValidationFailsWhenDatumPlacanjaIsNull() {
        placanje.setDatumPlacanja(null);
        assertValidationViolations("datumPlacanja", "Datum placanja je obavezan.");
    }

    @Test
    void testValidationFailsWhenRezervacijaIsNull() {
        placanje.setRezervacija(null);
        assertValidationViolations("rezervacija", "Rezervacija je obavezna.");
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
        String result = placanje.toString();
        assertTrue(result.contains("idPlacanje=1"));
        assertTrue(result.contains("status=COMPLETED"));
        assertTrue(result.contains("nacinPlacanja=CARD"));
        assertTrue(result.contains("iznos=100.0"));
        assertTrue(result.contains("datumPlacanja=2026-07-05"));
        assertFalse(result.contains("rezervacija"));
    }
}
