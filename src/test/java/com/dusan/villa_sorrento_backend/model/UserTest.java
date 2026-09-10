package com.dusan.villa_sorrento_backend.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final Validator validator;
    private User user;

    UserTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        user = validUser();
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
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(expectedMessages.length, violations.size());
        assertTrue(violations.stream()
                .allMatch(violation -> propertyName.equals(violation.getPropertyPath().toString())));

        Set<String> actualMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
        assertEquals(Set.of(expectedMessages), actualMessages);
    }

    @Test
    void testSetIdUser() {
        user.setIdUser(2L);
        assertEquals(2L, user.getIdUser());
    }

    @Test
    void testSetUsername() {
        user.setUsername("marko");
        assertEquals("marko", user.getUsername());
    }

    @Test
    void testSetPassword() {
        user.setPassword("abcd");
        assertEquals("abcd", user.getPassword());
    }

    @Test
    void testSetUloga() {
        user.setUloga("admin");
        assertEquals("admin", user.getUloga());
    }

    @Test
    void testSetBrojIsprave() {
        user.setBrojIsprave("98765");
        assertEquals("98765", user.getBrojIsprave());
    }

    @Test
    void testSetBrojTelefona() {
        user.setBrojTelefona("061222333");
        assertEquals("061222333", user.getBrojTelefona());
    }

    @Test
    void testSetRezervacije() {
        Set<Rezervacija> rezervacije = new HashSet<>();
        rezervacije.add(new Rezervacija());
        user.setRezervacije(rezervacije);
        assertEquals(rezervacije, user.getRezervacije());
    }

    @Test
    void testValidationPassesForValidUser() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenUsernameIsBlank() {
        user.setUsername("   ");
        assertValidationViolations("username", "Korisnicko ime je obavezno.");
    }

    @Test
    void testValidationFailsWhenUsernameIsTooShort() {
        user.setUsername("ab");
        assertValidationViolations("username", "Korisnicko ime mora imati izmedju 3 i 15 karaktera.");
    }

    @Test
    void testValidationFailsWhenUsernameIsTooLong() {
        user.setUsername("abcdefghijklmnop");
        assertValidationViolations("username", "Korisnicko ime mora imati izmedju 3 i 15 karaktera.");
    }

    @Test
    void testValidationFailsWhenPasswordIsBlank() {
        user.setPassword("    ");
        assertValidationViolations("password", "Lozinka je obavezna.");
    }

    @Test
    void testValidationFailsWhenPasswordIsTooShort() {
        user.setPassword("123");
        assertValidationViolations("password", "Lozinka mora imati najmanje 4 karaktera.");
    }

    @Test
    void testValidationFailsWhenUlogaIsBlank() {
        user.setUloga("     ");
        assertValidationViolations("uloga",
                "Uloga je obavezna.",
                "Uloga mora biti admin ili klijent.");
    }

    @Test
    void testValidationFailsWhenUlogaIsInvalid() {
        user.setUloga("menadzer");
        assertValidationViolations("uloga", "Uloga mora biti admin ili klijent.");
    }

    @Test
    void testValidationFailsWhenBrojIspraveIsBlank() {
        user.setBrojIsprave("     ");
        assertValidationViolations("brojIsprave",
                "Broj isprave je obavezan.",
                "Broj isprave sme sadrzati samo cifre.");
    }

    @Test
    void testValidationFailsWhenBrojIspraveIsTooShort() {
        user.setBrojIsprave("1234");
        assertValidationViolations("brojIsprave", "Broj isprave mora imati izmedju 5 i 20 cifara");
    }

    @Test
    void testValidationFailsWhenBrojIspraveIsTooLong() {
        user.setBrojIsprave("123456789012345678901");
        assertValidationViolations("brojIsprave", "Broj isprave mora imati izmedju 5 i 20 cifara");
    }

    @Test
    void testValidationFailsWhenBrojIspraveContainsLetters() {
        user.setBrojIsprave("ABC12");
        assertValidationViolations("brojIsprave", "Broj isprave sme sadrzati samo cifre.");
    }

    @Test
    void testValidationFailsWhenBrojTelefonaIsBlank() {
        user.setBrojTelefona("     ");
        assertValidationViolations("brojTelefona",
                "Broj telefona je obavezan.",
                "Broj telefona sme sadrzati samo cifre.");
    }

    @Test
    void testValidationFailsWhenBrojTelefonaIsTooShort() {
        user.setBrojTelefona("1234");
        assertValidationViolations("brojTelefona", "Broj telefona mora imati izmedju 5 i 20 cifara");
    }

    @Test
    void testValidationFailsWhenBrojTelefonaIsTooLong() {
        user.setBrojTelefona("123456789012345678901");
        assertValidationViolations("brojTelefona", "Broj telefona mora imati izmedju 5 i 20 cifara");
    }

    @Test
    void testValidationFailsWhenBrojTelefonaContainsLetters() {
        user.setBrojTelefona("060ABC");
        assertValidationViolations("brojTelefona", "Broj telefona sme sadrzati samo cifre.");
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = validUser();
        User user2 = validUser();
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testToStringContainsBasicFieldsAndExcludesRelations() {
        user.getRezervacije().add(new Rezervacija());
        String result = user.toString();
        assertTrue(result.contains("idUser=1"));
        assertTrue(result.contains("username=klijent1"));
        assertTrue(result.contains("password=1234"));
        assertTrue(result.contains("uloga=klijent"));
        assertTrue(result.contains("brojIsprave=12345"));
        assertTrue(result.contains("brojTelefona=060123456"));
        assertFalse(result.contains("rezervacije"));
    }
}
