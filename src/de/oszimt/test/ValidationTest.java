package de.oszimt.test;

import de.oszimt.util.Validation;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValidationTest {

    @Test
    public void testCheckIfLetters() throws Exception {

    }

    @Test
    public void testCheckIfCity() throws Exception {
        assertTrue(Validation.checkIfCity("Friedersdorf"));
    }

    @Test
    public void testCheckIfZipCode() throws Exception {
        assertTrue(Validation.checkIfZipCode("12345678"));
    }

    @Test
    public void testCheckIfStreetnr() throws Exception {

    }

    @Test
    public void testCheckIfStreet() throws Exception {

    }
}