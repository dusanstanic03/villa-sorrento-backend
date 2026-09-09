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

class UslugaTest {

    private final Validator validator;

    UslugaTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
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

    private void assertInvalid(Usluga usluga) {
        Set<ConstraintViolation<Usluga>> violations = validator.validate(usluga);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSetIdUsluga() {
        Usluga usluga = validUsluga();
        usluga.setIdUsluga(2L);
        assertEquals(2L, usluga.getIdUsluga());
    }

    @Test
    void testSetNaziv() {
        Usluga usluga = validUsluga();
        usluga.setNaziv("Parking");
        assertEquals("Parking", usluga.getNaziv());
    }

    @Test
    void testSetOpis() {
        Usluga usluga = validUsluga();
        usluga.setOpis("Parking mesto");
        assertEquals("Parking mesto", usluga.getOpis());
    }

    @Test
    void testSetCena() {
        Usluga usluga = validUsluga();
        usluga.setCena(20.0);
        assertEquals(20.0, usluga.getCena());
    }

    @Test
    void testSetAktivna() {
        Usluga usluga = validUsluga();
        usluga.setAktivna(false);
        assertFalse(usluga.getAktivna());
    }

    @Test
    void testSetStavkeRezervacije() {
        Usluga usluga = validUsluga();
        Set<StavkaRezervacije> stavke = new HashSet<>();
        stavke.add(new StavkaRezervacije());
        usluga.setStavkeRezervacije(stavke);
        assertEquals(stavke, usluga.getStavkeRezervacije());
    }

    @Test
    void testValidationPassesForValidUsluga() {
        Usluga usluga = validUsluga();
        Set<ConstraintViolation<Usluga>> violations = validator.validate(usluga);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenNazivIsBlank() {
        Usluga usluga = validUsluga();
        usluga.setNaziv("");
        assertInvalid(usluga);
    }

    @Test
    void testValidationFailsWhenNazivIsTooShort() {
        Usluga usluga = validUsluga();
        usluga.setNaziv("A");
        assertInvalid(usluga);
    }

    @Test
    void testValidationFailsWhenNazivIsTooLong() {
        Usluga usluga = validUsluga();
        usluga.setNaziv("a".repeat(51));
        assertInvalid(usluga);
    }

    @Test
    void testValidationFailsWhenOpisIsBlank() {
        Usluga usluga = validUsluga();
        usluga.setOpis("");
        assertInvalid(usluga);
    }

    @Test
    void testValidationFailsWhenOpisIsTooLong() {
        Usluga usluga = validUsluga();
        usluga.setOpis("a".repeat(501));
        assertInvalid(usluga);
    }

    @Test
    void testValidationFailsWhenCenaIsNull() {
        Usluga usluga = validUsluga();
        usluga.setCena(null);
        assertInvalid(usluga);
    }

    @Test
    void testValidationFailsWhenCenaIsNegative() {
        Usluga usluga = validUsluga();
        usluga.setCena(-1.0);
        assertInvalid(usluga);
    }

    @Test
    void testValidationFailsWhenAktivnaIsNull() {
        Usluga usluga = validUsluga();
        usluga.setAktivna(null);
        assertInvalid(usluga);
    }

    @Test
    void testEqualsAndHashCode() {
        Usluga usluga1 = validUsluga();
        Usluga usluga2 = validUsluga();
        assertEquals(usluga1, usluga2);
        assertEquals(usluga1.hashCode(), usluga2.hashCode());
    }

    @Test
    void testToStringContainsBasicFieldsAndExcludesRelations() {
        Usluga usluga = validUsluga();
        usluga.getStavkeRezervacije().add(new StavkaRezervacije());
        String result = usluga.toString();
        assertTrue(result.contains("idUsluga=1"));
        assertTrue(result.contains("naziv=Dorucak"));
        assertTrue(result.contains("opis=Dorucak za gosta"));
        assertTrue(result.contains("cena=12.0"));
        assertTrue(result.contains("aktivna=true"));
        assertFalse(result.contains("stavkeRezervacije"));
    }
}