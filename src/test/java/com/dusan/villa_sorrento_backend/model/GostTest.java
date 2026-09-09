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

class GostTest {

    private final Validator validator;

    GostTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
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

    private void assertInvalid(Gost gost) {
        Set<ConstraintViolation<Gost>> violations = validator.validate(gost);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSetIdGost() {
        Gost gost = validGost();
        gost.setIdGost(2L);
        assertEquals(2L, gost.getIdGost());
    }

    @Test
    void testSetIme() {
        Gost gost = validGost();
        gost.setIme("Marko");
        assertEquals("Marko", gost.getIme());
    }

    @Test
    void testSetPrezime() {
        Gost gost = validGost();
        gost.setPrezime("Markovic");
        assertEquals("Markovic", gost.getPrezime());
    }

    @Test
    void testSetBrojIsprave() {
        Gost gost = validGost();
        gost.setBrojIsprave("98765");
        assertEquals("98765", gost.getBrojIsprave());
    }

    @Test
    void testSetBrojTelefona() {
        Gost gost = validGost();
        gost.setBrojTelefona("061222333");
        assertEquals("061222333", gost.getBrojTelefona());
    }

    @Test
    void testSetStavkeRezervacije() {
        Gost gost = validGost();
        Set<StavkaRezervacije> stavke = new HashSet<>();
        stavke.add(new StavkaRezervacije());
        gost.setStavkeRezervacije(stavke);
        assertEquals(stavke, gost.getStavkeRezervacije());
    }

    @Test
    void testValidationPassesForValidGost() {
        Gost gost = validGost();
        Set<ConstraintViolation<Gost>> violations = validator.validate(gost);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenImeIsBlank() {
        Gost gost = validGost();
        gost.setIme("");
        assertInvalid(gost);
    }

    @Test
    void testValidationFailsWhenImeIsTooShort() {
        Gost gost = validGost();
        gost.setIme("Pe");
        assertInvalid(gost);
    }

    @Test
    void testValidationFailsWhenImeIsTooLong() {
        Gost gost = validGost();
        gost.setIme("abcdefghijklmnop");
        assertInvalid(gost);
    }

    @Test
    void testValidationFailsWhenPrezimeIsBlank() {
        Gost gost = validGost();
        gost.setPrezime("");
        assertInvalid(gost);
    }

    @Test
    void testValidationFailsWhenPrezimeIsTooShort() {
        Gost gost = validGost();
        gost.setPrezime("Pe");
        assertInvalid(gost);
    }

    @Test
    void testValidationFailsWhenPrezimeIsTooLong() {
        Gost gost = validGost();
        gost.setPrezime("abcdefghijklmnop");
        assertInvalid(gost);
    }

    @Test
    void testValidationFailsWhenBrojIspraveIsBlank() {
        Gost gost = validGost();
        gost.setBrojIsprave("");
        assertInvalid(gost);
    }

    @Test
    void testValidationFailsWhenBrojIspraveIsTooShort() {
        Gost gost = validGost();
        gost.setBrojIsprave("1234");
        assertInvalid(gost);
    }

    @Test
    void testValidationFailsWhenBrojIspraveIsTooLong() {
        Gost gost = validGost();
        gost.setBrojIsprave("123456789012345678901");
        assertInvalid(gost);
    }

    @Test
    void testValidationFailsWhenBrojIspraveContainsLetters() {
        Gost gost = validGost();
        gost.setBrojIsprave("ABC123");
        assertInvalid(gost);
    }

    @Test
    void testValidationFailsWhenBrojTelefonaIsBlank() {
        Gost gost = validGost();
        gost.setBrojTelefona("");
        assertInvalid(gost);
    }

    @Test
    void testValidationFailsWhenBrojTelefonaIsTooShort() {
        Gost gost = validGost();
        gost.setBrojTelefona("1234");
        assertInvalid(gost);
    }

    @Test
    void testValidationFailsWhenBrojTelefonaIsTooLong() {
        Gost gost = validGost();
        gost.setBrojTelefona("123456789012345678901");
        assertInvalid(gost);
    }

    @Test
    void testValidationFailsWhenBrojTelefonaContainsLetters() {
        Gost gost = validGost();
        gost.setBrojTelefona("060ABC");
        assertInvalid(gost);
    }

    @Test
    void testEqualsAndHashCode() {
        Gost gost1 = validGost();
        Gost gost2 = validGost();
        assertEquals(gost1, gost2);
        assertEquals(gost1.hashCode(), gost2.hashCode());
    }

    @Test
    void testToStringContainsBasicFieldsAndExcludesRelations() {
        Gost gost = validGost();
        gost.getStavkeRezervacije().add(new StavkaRezervacije());
        String result = gost.toString();
        assertTrue(result.contains("idGost=1"));
        assertTrue(result.contains("ime=Petar"));
        assertTrue(result.contains("prezime=Petrovic"));
        assertTrue(result.contains("brojIsprave=12345"));
        assertTrue(result.contains("brojTelefona=060123456"));
        assertFalse(result.contains("stavkeRezervacije"));
    }
}
