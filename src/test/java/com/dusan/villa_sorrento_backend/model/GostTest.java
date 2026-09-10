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

class GostTest {
    private Gost gost;
    private final Validator validator;

    GostTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        gost = validGost();
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

    private void assertValidationViolations(String propertyName, String... expectedMessages) {
        Set<ConstraintViolation<Gost>> violations = validator.validate(gost);
        assertEquals(expectedMessages.length, violations.size());
        assertTrue(violations.stream()
                .allMatch(violation -> propertyName.equals(violation.getPropertyPath().toString())));

        Set<String> actualMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
        assertEquals(Set.of(expectedMessages), actualMessages);
    }

    @Test
    void testSetIdGost() {
        gost.setIdGost(2L);
        assertEquals(2L, gost.getIdGost());
    }

    @Test
    void testSetIme() {
        gost.setIme("Marko");
        assertEquals("Marko", gost.getIme());
    }

    @Test
    void testSetPrezime() {
        gost.setPrezime("Markovic");
        assertEquals("Markovic", gost.getPrezime());
    }

    @Test
    void testSetBrojIsprave() {
        gost.setBrojIsprave("98765");
        assertEquals("98765", gost.getBrojIsprave());
    }

    @Test
    void testSetBrojTelefona() {
        gost.setBrojTelefona("061222333");
        assertEquals("061222333", gost.getBrojTelefona());
    }

    @Test
    void testSetStavkeRezervacije() {
        Set<StavkaRezervacije> stavke = new HashSet<>();
        stavke.add(new StavkaRezervacije());
        gost.setStavkeRezervacije(stavke);
        assertEquals(stavke, gost.getStavkeRezervacije());
    }

    @Test
    void testValidationPassesForValidGost() {
        Set<ConstraintViolation<Gost>> violations = validator.validate(gost);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenImeIsBlank() {
        gost.setIme("   ");
        assertValidationViolations("ime", "Ime gosta je obavezno.");
    }

    @Test
    void testValidationFailsWhenImeIsTooShort() {
        gost.setIme("Pe");
        assertValidationViolations("ime", "Ime mora imati izmedju 3 i 15 slova.");
    }

    @Test
    void testValidationFailsWhenImeIsTooLong() {
        gost.setIme("abcdefghijklmnop");
        assertValidationViolations("ime", "Ime mora imati izmedju 3 i 15 slova.");
    }

    @Test
    void testValidationFailsWhenPrezimeIsBlank() {
        gost.setPrezime("   ");
        assertValidationViolations("prezime", "Prezime gosta je obavezno.");
    }

    @Test
    void testValidationFailsWhenPrezimeIsTooShort() {
        gost.setPrezime("Pe");
        assertValidationViolations("prezime", "Prezime mora imati izmedju 3 i 15 slova.");
    }

    @Test
    void testValidationFailsWhenPrezimeIsTooLong() {
        gost.setPrezime("abcdefghijklmnop");
        assertValidationViolations("prezime", "Prezime mora imati izmedju 3 i 15 slova.");
    }

    @Test
    void testValidationFailsWhenBrojIspraveIsBlank() {
        gost.setBrojIsprave("     ");
        assertValidationViolations("brojIsprave",
                "Broj isprave gosta je obavezan.",
                "Broj isprave sme sadrzati samo cifre.");
    }

    @Test
    void testValidationFailsWhenBrojIspraveIsTooShort() {
        gost.setBrojIsprave("1234");
        assertValidationViolations("brojIsprave", "Broj isprave mora imati izmedju 5 i 20 cifara.");
    }

    @Test
    void testValidationFailsWhenBrojIspraveIsTooLong() {
        gost.setBrojIsprave("123456789012345678901");
        assertValidationViolations("brojIsprave", "Broj isprave mora imati izmedju 5 i 20 cifara.");
    }

    @Test
    void testValidationFailsWhenBrojIspraveContainsLetters() {
        gost.setBrojIsprave("ABC12");
        assertValidationViolations("brojIsprave", "Broj isprave sme sadrzati samo cifre.");
    }

    @Test
    void testValidationFailsWhenBrojTelefonaIsBlank() {
        gost.setBrojTelefona("     ");
        assertValidationViolations("brojTelefona",
                "Broj telefona gosta je obavezan.",
                "Broj telefona sme sadrzati samo cifre.");
    }

    @Test
    void testValidationFailsWhenBrojTelefonaIsTooShort() {
        gost.setBrojTelefona("1234");
        assertValidationViolations("brojTelefona", "Broj telefona mora imati izmedju 5 i 20 cifara.");
    }

    @Test
    void testValidationFailsWhenBrojTelefonaIsTooLong() {
        gost.setBrojTelefona("123456789012345678901");
        assertValidationViolations("brojTelefona", "Broj telefona mora imati izmedju 5 i 20 cifara.");
    }

    @Test
    void testValidationFailsWhenBrojTelefonaContainsLetters() {
        gost.setBrojTelefona("060ABC");
        assertValidationViolations("brojTelefona", "Broj telefona sme sadrzati samo cifre.");
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
