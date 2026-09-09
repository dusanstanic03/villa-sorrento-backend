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
import org.junit.jupiter.api.Test;

class SobaTest {

    private final Validator validator;

    SobaTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
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

    private void assertInvalid(Soba soba) {
        Set<ConstraintViolation<Soba>> violations = validator.validate(soba);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSetIdSoba() {
        Soba soba = validSoba();
        soba.setIdSoba(2L);
        assertEquals(2L, soba.getIdSoba());
    }

    @Test
    void testSetOpis() {
        Soba soba = validSoba();
        soba.setOpis("Standard soba");
        assertEquals("Standard soba", soba.getOpis());
    }

    @Test
    void testSetCena() {
        Soba soba = validSoba();
        soba.setCena(150.0);
        assertEquals(150.0, soba.getCena());
    }

    @Test
    void testSetJedinicaMere() {
        Soba soba = validSoba();
        soba.setJedinicaMere("noc");
        assertEquals("noc", soba.getJedinicaMere());
    }

    @Test
    void testSetDostupna() {
        Soba soba = validSoba();
        soba.setDostupna(false);
        assertFalse(soba.getDostupna());
    }

    @Test
    void testSetSlikaUrl() {
        Soba soba = validSoba();
        soba.setSlikaUrl("nova-slika.jpg");
        assertEquals("nova-slika.jpg", soba.getSlikaUrl());
    }

    @Test
    void testSetTipSobe() {
        Soba soba = validSoba();
        TipSobe tipSobe = validTipSobe();
        tipSobe.setNaziv("standard");
        soba.setTipSobe(tipSobe);
        assertEquals(tipSobe, soba.getTipSobe());
    }

    @Test
    void testSetStavkeRezervacije() {
        Soba soba = validSoba();
        Set<StavkaRezervacije> stavke = new HashSet<>();
        stavke.add(new StavkaRezervacije());
        soba.setStavkeRezervacije(stavke);
        assertEquals(stavke, soba.getStavkeRezervacije());
    }

    @Test
    void testValidationPassesForValidSoba() {
        Soba soba = validSoba();
        Set<ConstraintViolation<Soba>> violations = validator.validate(soba);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenOpisIsBlank() {
        Soba soba = validSoba();
        soba.setOpis("");
        assertInvalid(soba);
    }

    @Test
    void testValidationFailsWhenOpisIsTooShort() {
        Soba soba = validSoba();
        soba.setOpis("Soba");
        assertInvalid(soba);
    }

    @Test
    void testValidationFailsWhenOpisIsTooLong() {
        Soba soba = validSoba();
        soba.setOpis("a".repeat(256));
        assertInvalid(soba);
    }

    @Test
    void testValidationFailsWhenCenaIsNull() {
        Soba soba = validSoba();
        soba.setCena(null);
        assertInvalid(soba);
    }

    @Test
    void testValidationFailsWhenCenaIsZero() {
        Soba soba = validSoba();
        soba.setCena(0.0);
        assertInvalid(soba);
    }

    @Test
    void testValidationFailsWhenCenaIsNegative() {
        Soba soba = validSoba();
        soba.setCena(-1.0);
        assertInvalid(soba);
    }

    @Test
    void testValidationFailsWhenJedinicaMereIsBlank() {
        Soba soba = validSoba();
        soba.setJedinicaMere("");
        assertInvalid(soba);
    }

    @Test
    void testValidationFailsWhenJedinicaMereIsInvalid() {
        Soba soba = validSoba();
        soba.setJedinicaMere("dan");
        assertInvalid(soba);
    }

    @Test
    void testValidationFailsWhenDostupnaIsNull() {
        Soba soba = validSoba();
        soba.setDostupna(null);
        assertInvalid(soba);
    }

    @Test
    void testValidationFailsWhenSlikaUrlIsBlank() {
        Soba soba = validSoba();
        soba.setSlikaUrl("");
        assertInvalid(soba);
    }

    @Test
    void testValidationFailsWhenTipSobeIsNull() {
        Soba soba = validSoba();
        soba.setTipSobe(null);
        assertInvalid(soba);
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
        Soba soba = validSoba();
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