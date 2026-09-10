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

class UslugaTest {

    private final Validator validator;
    private Usluga usluga;

    UslugaTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        usluga = validUsluga();
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

    private void assertValidationViolations(String propertyName, String... expectedMessages) {
        Set<ConstraintViolation<Usluga>> violations = validator.validate(usluga);
        assertEquals(expectedMessages.length, violations.size());
        assertTrue(violations.stream()
                .allMatch(violation -> propertyName.equals(violation.getPropertyPath().toString())));

        Set<String> actualMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
        assertEquals(Set.of(expectedMessages), actualMessages);
    }

    @Test
    void testSetIdUsluga() {
        usluga.setIdUsluga(2L);
        assertEquals(2L, usluga.getIdUsluga());
    }

    @Test
    void testSetNaziv() {
        usluga.setNaziv("Parking");
        assertEquals("Parking", usluga.getNaziv());
    }

    @Test
    void testSetOpis() {
        usluga.setOpis("Parking mesto");
        assertEquals("Parking mesto", usluga.getOpis());
    }

    @Test
    void testSetCena() {
        usluga.setCena(20.0);
        assertEquals(20.0, usluga.getCena());
    }

    @Test
    void testSetAktivna() {
        usluga.setAktivna(false);
        assertFalse(usluga.getAktivna());
    }

    @Test
    void testSetStavkeRezervacije() {
        Set<StavkaRezervacije> stavke = new HashSet<>();
        stavke.add(new StavkaRezervacije());
        usluga.setStavkeRezervacije(stavke);
        assertEquals(stavke, usluga.getStavkeRezervacije());
    }

    @Test
    void testValidationPassesForValidUsluga() {
        Set<ConstraintViolation<Usluga>> violations = validator.validate(usluga);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenNazivIsBlank() {
        usluga.setNaziv("  ");
        assertValidationViolations("naziv", "Naziv usluge je obavezan.");
    }

    @Test
    void testValidationFailsWhenNazivIsTooShort() {
        usluga.setNaziv("A");
        assertValidationViolations("naziv", "Naziv usluge mora imati izmedju 2 i 50 karaktera.");
    }

    @Test
    void testValidationFailsWhenNazivIsTooLong() {
        usluga.setNaziv("a".repeat(51));
        assertValidationViolations("naziv", "Naziv usluge mora imati izmedju 2 i 50 karaktera.");
    }

    @Test
    void testValidationFailsWhenOpisIsBlank() {
        usluga.setOpis(" ");
        assertValidationViolations("opis", "Opis usluge je obavezan.");
    }

    @Test
    void testValidationFailsWhenOpisIsTooLong() {
        usluga.setOpis("a".repeat(501));
        assertValidationViolations("opis", "Opis usluge ne sme imati vise od 500 karaktera.");
    }

    @Test
    void testValidationFailsWhenCenaIsNull() {
        usluga.setCena(null);
        assertValidationViolations("cena", "Cena usluge je obavezna.");
    }

    @Test
    void testValidationFailsWhenCenaIsNegative() {
        usluga.setCena(-1.0);
        assertValidationViolations("cena", "Cena usluge ne sme biti negativna.");
    }

    @Test
    void testValidationFailsWhenAktivnaIsNull() {
        usluga.setAktivna(null);
        assertValidationViolations("aktivna", "Status aktivnosti usluge je obavezan.");
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
