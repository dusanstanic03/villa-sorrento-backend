package com.dusan.villa_sorrento_backend.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final Validator validator;

    UserTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
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

    private void assertInvalid(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testSetIdUser() {
        User user = validUser();
        user.setIdUser(2L);
        assertEquals(2L, user.getIdUser());
    }

    @Test
    void testSetUsername() {
        User user = validUser();
        user.setUsername("marko");
        assertEquals("marko", user.getUsername());
    }

    @Test
    void testSetPassword() {
        User user = validUser();
        user.setPassword("abcd");
        assertEquals("abcd", user.getPassword());
    }

    @Test
    void testSetUloga() {
        User user = validUser();
        user.setUloga("admin");
        assertEquals("admin", user.getUloga());
    }

    @Test
    void testSetBrojIsprave() {
        User user = validUser();
        user.setBrojIsprave("98765");
        assertEquals("98765", user.getBrojIsprave());
    }

    @Test
    void testSetBrojTelefona() {
        User user = validUser();
        user.setBrojTelefona("061222333");
        assertEquals("061222333", user.getBrojTelefona());
    }

    @Test
    void testSetRezervacije() {
        User user = validUser();
        Set<Rezervacija> rezervacije = new HashSet<>();
        rezervacije.add(new Rezervacija());
        user.setRezervacije(rezervacije);
        assertEquals(rezervacije, user.getRezervacije());
    }

    @Test
    void testValidationPassesForValidUser() {
        Set<ConstraintViolation<User>> violations = validator.validate(validUser());
        assertTrue(violations.isEmpty());
    }

    @Test
    void testValidationFailsWhenUsernameIsBlank() {
        User user = validUser();
        user.setUsername("");
        assertInvalid(user);
    }

    @Test
    void testValidationFailsWhenUsernameIsTooShort() {
        User user = validUser();
        user.setUsername("ab");
        assertInvalid(user);
    }

    @Test
    void testValidationFailsWhenUsernameIsTooLong() {
        User user = validUser();
        user.setUsername("abcdefghijklmnop");
        assertInvalid(user);
    }

    @Test
    void testValidationFailsWhenPasswordIsBlank() {
        User user = validUser();
        user.setPassword("");
        assertInvalid(user);
    }

    @Test
    void testValidationFailsWhenPasswordIsTooShort() {
        User user = validUser();
        user.setPassword("123");
        assertInvalid(user);
    }

    @Test
    void testValidationFailsWhenUlogaIsBlank() {
        User user = validUser();
        user.setUloga("");
        assertInvalid(user);
    }

    @Test
    void testValidationFailsWhenUlogaIsInvalid() {
        User user = validUser();
        user.setUloga("menadzer");
        assertInvalid(user);
    }

    @Test
    void testValidationFailsWhenBrojIspraveIsBlank() {
        User user = validUser();
        user.setBrojIsprave("");
        assertInvalid(user);
    }

    @Test
    void testValidationFailsWhenBrojIspraveIsTooShort() {
        User user = validUser();
        user.setBrojIsprave("1234");
        assertInvalid(user);
    }

    @Test
    void testValidationFailsWhenBrojIspraveIsTooLong() {
        User user = validUser();
        user.setBrojIsprave("123456789012345678901");
        assertInvalid(user);
    }

    @Test
    void testValidationFailsWhenBrojIspraveContainsLetters() {
        User user = validUser();
        user.setBrojIsprave("ABC123");
        assertInvalid(user);
    }

    @Test
    void testValidationFailsWhenBrojTelefonaIsBlank() {
        User user = validUser();
        user.setBrojTelefona("");
        assertInvalid(user);
    }

    @Test
    void testValidationFailsWhenBrojTelefonaIsTooShort() {
        User user = validUser();
        user.setBrojTelefona("1234");
        assertInvalid(user);
    }

    @Test
    void testValidationFailsWhenBrojTelefonaIsTooLong() {
        User user = validUser();
        user.setBrojTelefona("123456789012345678901");
        assertInvalid(user);
    }

    @Test
    void testValidationFailsWhenBrojTelefonaContainsLetters() {
        User user = validUser();
        user.setBrojTelefona("060ABC");
        assertInvalid(user);
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
        User user = validUser();
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
