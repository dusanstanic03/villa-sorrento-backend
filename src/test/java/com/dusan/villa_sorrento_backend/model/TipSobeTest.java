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

class TipSobeTest {

    private final Validator validator;
    private TipSobe tipSobe;

    TipSobeTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        tipSobe = validTipSobe();
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
        Set<ConstraintViolation<TipSobe>> violations = validator.validate(tipSobe);
        assertEquals(expectedMessages.length, violations.size());
        assertTrue(violations.stream()
                .allMatch(violation -> propertyName.equals(violation.getPropertyPath().toString())));

        Set<String> actualMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
        assertEquals(Set.of(expectedMessages), actualMessages);
    }

    @Test
    void testSetIdTipSobe() {
        tipSobe.setIdTipSobe(2L);
        assertEquals(2L, tipSobe.getIdTipSobe());
    }

    @Test
    void testSetNaziv() {
        tipSobe.setNaziv("apartman");
        assertEquals("apartman", tipSobe.getNaziv());
    }

    @Test
    void testSetOpis() {
        tipSobe.setOpis("Apartman sa pogledom");
        assertEquals("Apartman sa pogledom", tipSobe.getOpis());
    }

    @Test
    void testSetKapacitet() {
        tipSobe.setKapacitet(4);
        assertEquals(4, tipSobe.getKapacitet());
    }

    @Test
    void testSetSobe() {
        Set<Soba> sobe = new HashSet<>();
        sobe.add(new Soba());
        tipSobe.setSobe(sobe);
        assertEquals(sobe, tipSobe.getSobe());
    }

    @Test
    void testValidationPassesForValidTipSobe() {
        Set<ConstraintViolation<TipSobe>> violations = validator.validate(tipSobe);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenNazivIsBlank() {
        tipSobe.setNaziv("   ");
        assertValidationViolations("naziv", "Naziv tipa sobe je obavezan.");
    }

    @Test
    void testValidationFailsWhenNazivIsTooShort() {
        tipSobe.setNaziv("ab");
        assertValidationViolations("naziv", "Naziv tipa sobe mora imati izmedju 3 i 50 slova.");
    }

    @Test
    void testValidationFailsWhenNazivIsTooLong() {
        tipSobe.setNaziv("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        assertValidationViolations("naziv", "Naziv tipa sobe mora imati izmedju 3 i 50 slova.");
    }

    @Test
    void testValidationFailsWhenOpisIsBlank() {
        tipSobe.setOpis(" ");
        assertValidationViolations("opis", "Opis tipa sobe je obavezan.");
    }

    @Test
    void testValidationFailsWhenOpisIsTooLong() {
        tipSobe.setOpis("a".repeat(501));
        assertValidationViolations("opis", "Opis tipa sobe ne sme imati vise od 500 karaktera.");
    }

    @Test
    void testValidationFailsWhenKapacitetIsNull() {
        tipSobe.setKapacitet(null);
        assertValidationViolations("kapacitet", "Kapacitet je obavezan.");
    }

    @Test
    void testValidationFailsWhenKapacitetIsLessThanOne() {
        tipSobe.setKapacitet(0);
        assertValidationViolations("kapacitet", "Kapacitet mora biti najmanje 1.");
    }

    @Test
    void testValidationFailsWhenKapacitetIsGreaterThanFour() {
        tipSobe.setKapacitet(5);
        assertValidationViolations("kapacitet", "Kapacitet ne sme biti veci od 4.");
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
        tipSobe.getSobe().add(new Soba());
        String result = tipSobe.toString();
        assertTrue(result.contains("idTipSobe=1"));
        assertTrue(result.contains("naziv=deluxe"));
        assertTrue(result.contains("opis=Deluxe soba"));
        assertTrue(result.contains("kapacitet=2"));
        assertFalse(result.contains("sobe"));
    }
}
