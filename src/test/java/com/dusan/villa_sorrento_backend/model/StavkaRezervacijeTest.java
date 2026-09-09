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

class StavkaRezervacijeTest {

    private final Validator validator;

    StavkaRezervacijeTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private StavkaRezervacije validStavkaRezervacije() {
        StavkaRezervacije stavka = new StavkaRezervacije();
        stavka.setId(1L);
        stavka.setRb(1);
        stavka.setDatumOd(LocalDate.of(2026, 8, 1));
        stavka.setDatumDo(LocalDate.of(2026, 8, 5));
        stavka.setIznos(400.0);
        stavka.setRezervacija(validRezervacija());
        stavka.setSoba(validSoba());
        stavka.setGosti(new HashSet<>());
        stavka.setUsluge(new HashSet<>());
        return stavka;
    }

    private Rezervacija validRezervacija() {
        Rezervacija rezervacija = new Rezervacija();
        rezervacija.setIdRezervacija(1L);
        rezervacija.setIznos(400.0);
        rezervacija.setDatumKreiranja(LocalDate.of(2026, 7, 1));
        rezervacija.setUser(validUser());
        rezervacija.setPlacanja(new HashSet<>());
        rezervacija.setStavkeRezervacije(new HashSet<>());
        return rezervacija;
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

    private Gost validGost() {
        Gost gost = new Gost();
        gost.setIdGost(1L);
        gost.setIme("Petar");
        gost.setPrezime("Petrovic");
        gost.setBrojIsprave("12345");
        gost.setBrojTelefona("060123456");
        gost.setStavkeRezervacije(new HashSet<>());
        return gost;
    }

    private Usluga validUsluga() {
        Usluga usluga = new Usluga();
        usluga.setIdUsluga(1L);
        usluga.setNaziv("Dorucak");
        usluga.setOpis("Dorucak za gosta");
        usluga.setCena(12.0);
        usluga.setAktivna(true);
        usluga.setStavkeRezervacije(new HashSet<>());
        return usluga;
    }

    private void assertInvalid(StavkaRezervacije stavka) {
        Set<ConstraintViolation<StavkaRezervacije>> violations = validator.validate(stavka);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSetId() {
        StavkaRezervacije stavka = validStavkaRezervacije();
        stavka.setId(2L);
        assertEquals(2L, stavka.getId());
    }

    @Test
    void testSetRb() {
        StavkaRezervacije stavka = validStavkaRezervacije();
        stavka.setRb(2);
        assertEquals(2, stavka.getRb());
    }

    @Test
    void testSetDatumOd() {
        StavkaRezervacije stavka = validStavkaRezervacije();
        LocalDate datum = LocalDate.of(2026, 9, 1);
        stavka.setDatumOd(datum);
        assertEquals(datum, stavka.getDatumOd());
    }

    @Test
    void testSetDatumDo() {
        StavkaRezervacije stavka = validStavkaRezervacije();
        LocalDate datum = LocalDate.of(2026, 9, 5);
        stavka.setDatumDo(datum);
        assertEquals(datum, stavka.getDatumDo());
    }

    @Test
    void testSetIznos() {
        StavkaRezervacije stavka = validStavkaRezervacije();
        stavka.setIznos(500.0);
        assertEquals(500.0, stavka.getIznos());
    }

    @Test
    void testSetRezervacija() {
        StavkaRezervacije stavka = validStavkaRezervacije();
        Rezervacija rezervacija = validRezervacija();
        rezervacija.setIdRezervacija(2L);
        stavka.setRezervacija(rezervacija);
        assertEquals(rezervacija, stavka.getRezervacija());
    }

    @Test
    void testSetSoba() {
        StavkaRezervacije stavka = validStavkaRezervacije();
        Soba soba = validSoba();
        soba.setIdSoba(2L);
        stavka.setSoba(soba);
        assertEquals(soba, stavka.getSoba());
    }

    @Test
    void testSetGosti() {
        StavkaRezervacije stavka = validStavkaRezervacije();
        Set<Gost> gosti = new HashSet<>();
        gosti.add(validGost());
        stavka.setGosti(gosti);
        assertEquals(gosti, stavka.getGosti());
    }

    @Test
    void testSetUsluge() {
        StavkaRezervacije stavka = validStavkaRezervacije();
        Set<Usluga> usluge = new HashSet<>();
        usluge.add(validUsluga());
        stavka.setUsluge(usluge);
        assertEquals(usluge, stavka.getUsluge());
    }

    @Test
    void testValidationPassesForValidStavkaRezervacije() {
        StavkaRezervacije stavka = validStavkaRezervacije();
        Set<ConstraintViolation<StavkaRezervacije>> violations = validator.validate(stavka);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenRbIsLessThanOne() {
        StavkaRezervacije stavka = validStavkaRezervacije();
        stavka.setRb(0);
        assertInvalid(stavka);
    }

    @Test
    void testValidationFailsWhenDatumOdIsNull() {
        StavkaRezervacije stavka = validStavkaRezervacije();
        stavka.setDatumOd(null);
        assertInvalid(stavka);
    }

    @Test
    void testValidationFailsWhenDatumDoIsNull() {
        StavkaRezervacije stavka = validStavkaRezervacije();
        stavka.setDatumDo(null);
        assertInvalid(stavka);
    }

    @Test
    void testValidationFailsWhenIznosIsNegative() {
        StavkaRezervacije stavka = validStavkaRezervacije();
        stavka.setIznos(-1.0);
        assertInvalid(stavka);
    }

    @Test
    void testValidationFailsWhenRezervacijaIsNull() {
        StavkaRezervacije stavka = validStavkaRezervacije();
        stavka.setRezervacija(null);
        assertInvalid(stavka);
    }

    @Test
    void testValidationFailsWhenSobaIsNull() {
        StavkaRezervacije stavka = validStavkaRezervacije();
        stavka.setSoba(null);
        assertInvalid(stavka);
    }

    @Test
    void testEqualsAndHashCode() {
        StavkaRezervacije stavka1 = validStavkaRezervacije();
        StavkaRezervacije stavka2 = validStavkaRezervacije();
        assertEquals(stavka1, stavka2);
        assertEquals(stavka1.hashCode(), stavka2.hashCode());
    }

    @Test
    void testToStringContainsBasicFieldsAndExcludesRelations() {
        StavkaRezervacije stavka = validStavkaRezervacije();
        stavka.getGosti().add(validGost());
        stavka.getUsluge().add(validUsluga());
        String result = stavka.toString();
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("rb=1"));
        assertTrue(result.contains("datumOd=2026-08-01"));
        assertTrue(result.contains("datumDo=2026-08-05"));
        assertTrue(result.contains("iznos=400.0"));
        assertFalse(result.contains("rezervacija"));
        assertFalse(result.contains("soba"));
        assertFalse(result.contains("gosti"));
        assertFalse(result.contains("usluge"));
    }
}