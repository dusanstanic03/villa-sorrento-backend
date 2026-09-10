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

class RezervacijaTest {

    private final Validator validator;
    private Rezervacija rezervacija;

    RezervacijaTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        rezervacija = validRezervacija();
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
        Set<ConstraintViolation<Rezervacija>> violations = validator.validate(rezervacija);
        assertEquals(expectedMessages.length, violations.size());
        assertTrue(violations.stream()
                .allMatch(violation -> propertyName.equals(violation.getPropertyPath().toString())));

        Set<String> actualMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
        assertEquals(Set.of(expectedMessages), actualMessages);
    }

    @Test
    void testSetIdRezervacija() {
        rezervacija.setIdRezervacija(2L);
        assertEquals(2L, rezervacija.getIdRezervacija());
    }

    @Test
    void testSetIznos() {
        rezervacija.setIznos(250.0);
        assertEquals(250.0, rezervacija.getIznos());
    }

    @Test
    void testSetDatumKreiranja() {
        LocalDate datum = LocalDate.of(2026, 8, 1);
        rezervacija.setDatumKreiranja(datum);
        assertEquals(datum, rezervacija.getDatumKreiranja());
    }

    @Test
    void testSetUser() {
        User user = validUser();
        user.setIdUser(2L);
        rezervacija.setUser(user);
        assertEquals(user, rezervacija.getUser());
    }

    @Test
    void testSetPlacanja() {
        Set<Placanje> placanja = new HashSet<>();
        placanja.add(new Placanje());
        rezervacija.setPlacanja(placanja);
        assertEquals(placanja, rezervacija.getPlacanja());
    }

    @Test
    void testSetStavkeRezervacije() {
        Set<StavkaRezervacije> stavke = new HashSet<>();
        stavke.add(new StavkaRezervacije());
        rezervacija.setStavkeRezervacije(stavke);
        assertEquals(stavke, rezervacija.getStavkeRezervacije());
    }

    @Test
    void testValidationPassesForValidRezervacija() {
        Set<ConstraintViolation<Rezervacija>> violations = validator.validate(rezervacija);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenIznosIsNegative() {
        rezervacija.setIznos(-1.0);
        assertValidationViolations("iznos", "Iznos rezervacije ne sme biti negativan.");
    }

    @Test
    void testValidationFailsWhenDatumKreiranjaIsNull() {
        rezervacija.setDatumKreiranja(null);
        assertValidationViolations("datumKreiranja", "Datum kreiranja rezervacije je obavezan.");
    }

    @Test
    void testValidationFailsWhenUserIsNull() {
        rezervacija.setUser(null);
        assertValidationViolations("user", "Klijent je obavezan.");
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
