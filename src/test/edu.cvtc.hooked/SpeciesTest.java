package edu.cvtc.hooked;

import edu.cvtc.hooked.model.Species;
import edu.cvtc.hooked.util.DbUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpeciesTest {

    static {
        System.setProperty("hooked.test.db", "true");
    }

    @BeforeAll
    static void setupDatabase() {
        // Make sure all tables exist so we can insert users and catches.
        System.setProperty("hooked.test.db", "true");
        DbUtil.ensureSchema();
    }

    @Test
    public void testDefaultConstructorDoesNotThrow() {
        // Just verify we can construct it and it starts in a "blank" state.
        Species s = new Species();

        // We don't assert specific defaults for numeric fields because
        // they may be primitive (0.0) or wrapper (null).
        // But we *can* safely check that the object exists.
        assertNotNull(s);
    }

    @Test
    public void testSettersAndGetters() {
        Species s = new Species();

        // These methods should exist on your current model class
        s.setSpeciesId(5);
        s.setSpeciesName("Walleye");
        s.setMaxLength(30.5);
        s.setMaxWeight(12.3);

        assertEquals(5, s.getSpeciesId());
        assertEquals("Walleye", s.getSpeciesName());
        assertEquals(30.5, s.getMaxLength());
        assertEquals(12.3, s.getMaxWeight());
    }

    @Test
    public void testMultipleInstancesHoldIndependentData() {
        Species s1 = new Species();
        s1.setSpeciesId(1);
        s1.setSpeciesName("Bluegill");
        s1.setMaxLength(12.0);
        s1.setMaxWeight(3.0);

        Species s2 = new Species();
        s2.setSpeciesId(2);
        s2.setSpeciesName("Northern Pike");
        s2.setMaxLength(40.0);
        s2.setMaxWeight(20.0);

        assertEquals(1, s1.getSpeciesId());
        assertEquals("Bluegill", s1.getSpeciesName());
        assertEquals(12.0, s1.getMaxLength());
        assertEquals(3.0, s1.getMaxWeight());

        assertEquals(2, s2.getSpeciesId());
        assertEquals("Northern Pike", s2.getSpeciesName());
        assertEquals(40.0, s2.getMaxLength());
        assertEquals(20.0, s2.getMaxWeight());
    }

    @Test
    public void testNameFormatting() {
        Species s = new Species();
        s.setSpeciesName("   largeMOUTH    bass   ");

        assertEquals("Largemouth Bass", s.getSpeciesName());
    }

    @Test
    public void testParameterizedConstructor() {
        Species s = new Species("crappie", 15.0, 2.0, 10);

        assertNull(s.getSpeciesId()); // Correct for constructor that doesn't take ID
        assertEquals("Crappie", s.getSpeciesName());
        assertEquals(15.0, s.getMaxLength());
        assertEquals(2.0, s.getMaxWeight());
        assertEquals(10, s.getCreatedByUserId());
    }
}
