package com.dusan.villa_sorrento_backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SobaTest {

    private final Validator validator;
    private Soba soba;

    SobaTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        soba = validSoba();
    }

    private Soba validSoba() {
        Soba soba = new Soba();
        soba.setIdSoba(1L);
        soba.setOpis("Deluxe soba");
        soba.setCena(100.0);
        soba.setJedinicaMere("noc");
        soba.setDostupna(true);
        soba.setSlikaUrl("slika.jpg");
        soba.setTipSobe(validTipSobe());
        soba.setStavkeRezervacije(new HashSet<>());
        return soba;
    }

    private TipSobe validTipSobe() {
        TipSobe tipSobe = new TipSobe();
        tipSobe.setIdTipSobe(1L);
        tipSobe.setNaziv("deluxe");
        tipSobe.setOpis("Deluxe soba");
        tipSobe.setKapacitet(2);
        tipSobe.setSobe(new HashSet<>());
        return tipSobe;
    }

    private void assertValidationViolations(String propertyName, String... expectedMessages) {
        Set<ConstraintViolation<Soba>> violations = validator.validate(soba);
        assertEquals(expectedMessages.length, violations.size());
        assertTrue(violations.stream()
                .allMatch(violation -> propertyName.equals(violation.getPropertyPath().toString())));

        Set<String> actualMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
        assertEquals(Set.of(expectedMessages), actualMessages);
    }

    @Test
    void testSetIdSoba() {
        soba.setIdSoba(2L);
        assertEquals(2L, soba.getIdSoba());
    }

    @Test
    void testSetOpis() {
        soba.setOpis("Standard soba");
        assertEquals("Standard soba", soba.getOpis());
    }

    @Test
    void testSetCena() {
        soba.setCena(150.0);
        assertEquals(150.0, soba.getCena());
    }

    @Test
    void testSetJedinicaMere() {
        soba.setJedinicaMere("noc");
        assertEquals("noc", soba.getJedinicaMere());
    }

    @Test
    void testSetDostupna() {
        soba.setDostupna(false);
        assertFalse(soba.getDostupna());
    }

    @Test
    void testSetSlikaUrl() {
        soba.setSlikaUrl("nova-slika.jpg");
        assertEquals("nova-slika.jpg", soba.getSlikaUrl());
    }

    @Test
    void testSetTipSobe() {
        TipSobe tipSobe = validTipSobe();
        tipSobe.setNaziv("standard");
        soba.setTipSobe(tipSobe);
        assertEquals(tipSobe, soba.getTipSobe());
    }

    @Test
    void testSetStavkeRezervacije() {
        Set<StavkaRezervacije> stavke = new HashSet<>();
        stavke.add(new StavkaRezervacije());
        soba.setStavkeRezervacije(stavke);
        assertEquals(stavke, soba.getStavkeRezervacije());
    }

    @Test
    void testValidationPassesForValidSoba() {
        Set<ConstraintViolation<Soba>> violations = validator.validate(soba);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenOpisIsBlank() {
        soba.setOpis("     ");
        assertValidationViolations("opis", "Opis sobe je obavezan.");
    }

    @Test
    void testValidationFailsWhenOpisIsTooShort() {
        soba.setOpis("Soba");
        assertValidationViolations("opis", "Opis sobe mora imati izmedju 5 i 255 karaktera.");
    }

    @Test
    void testValidationFailsWhenOpisIsTooLong() {
        soba.setOpis("a".repeat(256));
        assertValidationViolations("opis", "Opis sobe mora imati izmedju 5 i 255 karaktera.");
    }

    @Test
    void testValidationFailsWhenCenaIsNull() {
        soba.setCena(null);
        assertValidationViolations("cena", "Cena sobe je obavezna.");
    }

    @Test
    void testValidationFailsWhenCenaIsZero() {
        soba.setCena(0.0);
        assertValidationViolations("cena", "Cena sobe mora biti pozitivna vrednost.");
    }

    @Test
    void testValidationFailsWhenCenaIsNegative() {
        soba.setCena(-1.0);
        assertValidationViolations("cena", "Cena sobe mora biti pozitivna vrednost.");
    }

    @Test
    void testValidationFailsWhenJedinicaMereIsBlank() {
        soba.setJedinicaMere("   ");
        assertValidationViolations("jedinicaMere",
                "Jedinica mere je obavezna.",
                "Jedinica mere mora biti noc.");
    }

    @Test
    void testValidationFailsWhenJedinicaMereIsInvalid() {
        soba.setJedinicaMere("dan");
        assertValidationViolations("jedinicaMere", "Jedinica mere mora biti noc.");
    }

    @Test
    void testValidationFailsWhenDostupnaIsNull() {
        soba.setDostupna(null);
        assertValidationViolations("dostupna", "Dostupnost sobe je obavezna.");
    }

    @Test
    void testValidationFailsWhenSlikaUrlIsBlank() {
        soba.setSlikaUrl(" ");
        assertValidationViolations("slikaUrl", "URL slike je obavezan.");
    }

    @Test
    void testValidationFailsWhenTipSobeIsNull() {
        soba.setTipSobe(null);
        assertValidationViolations("tipSobe", "Tip sobe je obavezan.");
    }

    @Test
    void testEqualsAndHashCode() {
        Soba soba1 = validSoba();
        Soba soba2 = validSoba();
        assertEquals(soba1, soba2);
        assertEquals(soba1.hashCode(), soba2.hashCode());
    }

    @Test
    void testToStringContainsBasicFieldsAndExcludesRelations() {
        soba.getStavkeRezervacije().add(new StavkaRezervacije());
        String result = soba.toString();
        assertTrue(result.contains("idSoba=1"));
        assertTrue(result.contains("opis=Deluxe soba"));
        assertTrue(result.contains("cena=100.0"));
        assertTrue(result.contains("jedinicaMere=noc"));
        assertTrue(result.contains("dostupna=true"));
        assertTrue(result.contains("slikaUrl=slika.jpg"));
        assertFalse(result.contains("tipSobe"));
        assertFalse(result.contains("stavkeRezervacije"));
    }
}
