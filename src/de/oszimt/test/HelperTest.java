package de.oszimt.test;

import de.oszimt.model.User;
import de.oszimt.ui.impl.tui.util.Helper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HelperTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCreateDummyUser() throws Exception {
        User user = Helper.createDummyUser();
        User dummy = new User("", "", null, "", "", "", 0, null);

        assertEquals(user, dummy);
    }

    @Test
    public void testToAscii() throws Exception {

    }

    @Test
    public void testUserParameterToArray() throws Exception {

    }

    @Test
    public void testGetMaxEntry() throws Exception {

    }

    @Test
    public void testSearchUsers() throws Exception {

    }

    @Test
    public void testGetDeclaredField() throws Exception {

    }

    @Test
    public void testGetObject() throws Exception {

    }
}