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

class TipSobeTest {

    private final Validator validator;

    TipSobeTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
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

    private void assertInvalid(TipSobe tipSobe) {
        Set<ConstraintViolation<TipSobe>> violations = validator.validate(tipSobe);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSetIdTipSobe() {
        TipSobe tipSobe = validTipSobe();
        tipSobe.setIdTipSobe(2L);
        assertEquals(2L, tipSobe.getIdTipSobe());
    }

    @Test
    void testSetNaziv() {
        TipSobe tipSobe = validTipSobe();
        tipSobe.setNaziv("apartman");
        assertEquals("apartman", tipSobe.getNaziv());
    }

    @Test
    void testSetOpis() {
        TipSobe tipSobe = validTipSobe();
        tipSobe.setOpis("Apartman sa pogledom");
        assertEquals("Apartman sa pogledom", tipSobe.getOpis());
    }

    @Test
    void testSetKapacitet() {
        TipSobe tipSobe = validTipSobe();
        tipSobe.setKapacitet(4);
        assertEquals(4, tipSobe.getKapacitet());
    }

    @Test
    void testSetSobe() {
        TipSobe tipSobe = validTipSobe();
        Set<Soba> sobe = new HashSet<>();
        sobe.add(new Soba());
        tipSobe.setSobe(sobe);
        assertEquals(sobe, tipSobe.getSobe());
    }

    @Test
    void testValidationPassesForValidTipSobe() {
        TipSobe tipSobe = validTipSobe();
        Set<ConstraintViolation<TipSobe>> violations = validator.validate(tipSobe);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenNazivIsBlank() {
        TipSobe tipSobe = validTipSobe();
        tipSobe.setNaziv("");
        assertInvalid(tipSobe);
    }

    @Test
    void testValidationFailsWhenNazivIsTooShort() {
        TipSobe tipSobe = validTipSobe();
        tipSobe.setNaziv("ab");
        assertInvalid(tipSobe);
    }

    @Test
    void testValidationFailsWhenNazivIsTooLong() {
        TipSobe tipSobe = validTipSobe();
        tipSobe.setNaziv("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        assertInvalid(tipSobe);
    }

    @Test
    void testValidationFailsWhenOpisIsBlank() {
        TipSobe tipSobe = validTipSobe();
        tipSobe.setOpis("");
        assertInvalid(tipSobe);
    }

    @Test
    void testValidationFailsWhenOpisIsTooLong() {
        TipSobe tipSobe = validTipSobe();
        tipSobe.setOpis("a".repeat(501));
        assertInvalid(tipSobe);
    }

    @Test
    void testValidationFailsWhenKapacitetIsNull() {
        TipSobe tipSobe = validTipSobe();
        tipSobe.setKapacitet(null);
        assertInvalid(tipSobe);
    }

    @Test
    void testValidationFailsWhenKapacitetIsLessThanOne() {
        TipSobe tipSobe = validTipSobe();
        tipSobe.setKapacitet(0);
        assertInvalid(tipSobe);
    }

    @Test
    void testValidationFailsWhenKapacitetIsGreaterThanFour() {
        TipSobe tipSobe = validTipSobe();
        tipSobe.setKapacitet(5);
        assertInvalid(tipSobe);
    }

    @Test
    void testEqualsAndHashCode() {
        TipSobe tipSobe1 = validTipSobe();
        TipSobe tipSobe2 = validTipSobe();
        assertEquals(tipSobe1, tipSobe2);
        assertEquals(tipSobe1.hashCode(), tipSobe2.hashCode());
    }

    @Test
    void testToStringContainsBasicFieldsAndExcludesRelations() {
        TipSobe tipSobe = validTipSobe();
        tipSobe.getSobe().add(new Soba());
        String result = tipSobe.toString();
        assertTrue(result.contains("idTipSobe=1"));
        assertTrue(result.contains("naziv=deluxe"));
        assertTrue(result.contains("opis=Deluxe soba"));
        assertTrue(result.contains("kapacitet=2"));
        assertFalse(result.contains("sobe"));
    }
}
