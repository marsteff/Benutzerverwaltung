package de.oszimt.test;

import de.oszimt.util.Validation;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Validation Test Klasse
 * Prueft ob die Validation an ihren Grenzbereichen immernoch ihre gueltigkeit besitzen
 */
public class ValidationTest {

    @Test
    public void testCheckIfLetters() throws Exception {
        //Tests, die True liefern
        assertTrue(Validation.checkIfLetters("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));

        //Tests, die False liefern
        assertFalse(Validation.checkIfLetters("!§$%&/()"));
        assertFalse(Validation.checkIfLetters("IchBinEinStr1ng"));
    }

    @Test
    public void testCheckIfCity() throws Exception {
        //Tests, die True liefern
        assertTrue(Validation.checkIfCity("Friedersdorf"));
        assertTrue(Validation.checkIfCity("Use-dom"));
        assertTrue(Validation.checkIfCity("Êster"));

        //Tests, die False liefern
        assertFalse(Validation.checkIfCity("Berlin1"));
        assertFalse(Validation.checkIfCity("Berlin!"));
        assertFalse(Validation.checkIfCity("Berlin§"));
    }

    @Test
    public void testCheckIfZipCode() throws Exception
    {
        //Tests, die True liefern
        assertTrue(Validation.checkIfZipCode("12345"));

        //Tests, die False liefern
        assertFalse(Validation.checkIfZipCode("1234"));
        assertFalse(Validation.checkIfZipCode("123456"));
        assertFalse(Validation.checkIfZipCode("1234A"));
        assertFalse(Validation.checkIfZipCode("1234!"));
    }

    @Test
    public void testCheckIfStreetnr() throws Exception {
        //Tests, die True liefern
        assertTrue(Validation.checkIfStreetnr("1"));
        assertTrue(Validation.checkIfStreetnr("12"));
        assertTrue(Validation.checkIfStreetnr("123"));
        assertTrue(Validation.checkIfStreetnr("12b"));

        //TODO herausfinden wo die Grenzbereiche fue Strassennummern sind
        //Tests, die False liefern
        assertFalse(Validation.checkIfStreetnr("1!"));

    }

    @Test
    public void testCheckIfStreet() throws Exception {
        //Tests, die True liefern
        assertTrue(Validation.checkIfStreet("Hauptstrasse"));
        assertTrue(Validation.checkIfStreet("Hauptstr."));
        assertTrue(Validation.checkIfStreet("Strasse Nummer 1"));
        assertTrue(Validation.checkIfStreet("Hauptstraße"));

        //Tests, die False liefern
        assertFalse(Validation.checkIfStreet("Haup!strasse"));
    }
}