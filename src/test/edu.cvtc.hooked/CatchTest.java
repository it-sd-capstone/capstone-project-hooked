package edu.cvtc.hooked;

import edu.cvtc.hooked.model.Catch;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CatchTest {

    @Test
    public void testDefaultConstructorStartsEmpty() {
        // Just checking that the empty constructor does not prefill anything.
        Catch c = new Catch();

        assertNull(c.getCatchId());
        assertNull(c.getUserId());
        assertNull(c.getSpeciesName());
        assertNull(c.getLocationName());
        assertNull(c.getBaitType());
        assertNull(c.getDateCaught());
        assertNull(c.getNotes());

        // length/weight use primitive double, so they default to 0.0
        assertEquals(0.0, c.getLength());
        assertEquals(0.0, c.getWeight());
    }

    @Test
    public void testFullConstructorSetsAllFields() {
        // Testing the constructor that sets everything including the ID.
        Catch c = new Catch(
                100,
                5,
                "Walleye",
                "Lake Superior",
                "Minnow",
                "2024-09-15",
                "Caught near rocks",
                22.5,
                4.2
        );

        assertEquals(100, c.getCatchId());
        assertEquals(5, c.getUserId());
        assertEquals("Walleye", c.getSpeciesName());
        assertEquals("Lake Superior", c.getLocationName());
        assertEquals("Minnow", c.getBaitType());
        assertEquals("2024-09-15", c.getDateCaught());
        assertEquals("Caught near rocks", c.getNotes());
        assertEquals(22.5, c.getLength());
        assertEquals(4.2, c.getWeight());
    }

    @Test
    public void testNewCatchConstructorHasNullId() {
        // The new catch constructor should leave catchId as null.
        Catch c = new Catch(
                8,
                "Bass",
                "River Bend",
                "Worm",
                "2024-10-01",
                "Shallow water",
                15.0,
                2.3
        );

        assertNull(c.getCatchId());
        assertEquals(8, c.getUserId());
        assertEquals("Bass", c.getSpeciesName());
        assertEquals("River Bend", c.getLocationName());
        assertEquals("Worm", c.getBaitType());
        assertEquals("2024-10-01", c.getDateCaught());
        assertEquals("Shallow water", c.getNotes());
        assertEquals(15.0, c.getLength());
        assertEquals(2.3, c.getWeight());
    }

    @Test
    public void testSettersAndGetters() {
        // Making sure all setters and getters behave correctly.
        Catch c = new Catch();

        c.setCatchId(10);
        c.setUserId(2);
        c.setSpeciesName("Pike");
        c.setLocationName("North Shore");
        c.setBaitType("Spinner");
        c.setDateCaught("2024-11-05");
        c.setNotes("Cold morning");
        c.setLength(30.0);
        c.setWeight(8.0);

        assertEquals(10, c.getCatchId());
        assertEquals(2, c.getUserId());
        assertEquals("Pike", c.getSpeciesName());
        assertEquals("North Shore", c.getLocationName());
        assertEquals("Spinner", c.getBaitType());
        assertEquals("2024-11-05", c.getDateCaught());
        assertEquals("Cold morning", c.getNotes());
        assertEquals(30.0, c.getLength());
        assertEquals(8.0, c.getWeight());
    }

    @Test
    public void testEqualsUsesCatchId() {
        // Two Catch objects with the same catchId should be equal.
        Catch a = new Catch(
                50,
                1,
                "Trout",
                "Creek",
                "Fly",
                "2024-01-01",
                "notes",
                12.0,
                1.1
        );

        Catch b = new Catch(
                50,   // same ID
                9,
                "Whatever",
                "Somewhere",
                "Anything",
                "2024-02-02",
                "More notes",
                20.0,
                2.0
        );

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testNotEqualWhenIdsDiffer() {
        // Different catchId values should not be equal.
        Catch a = new Catch(
                1,
                1,
                "Species",
                "Loc",
                "Bait",
                "2024-01-01",
                "n1",
                10,
                1
        );

        Catch b = new Catch(
                2,
                1,
                "Species",
                "Loc",
                "Bait",
                "2024-01-01",
                "n1",
                10,
                1
        );

        assertNotEquals(a, b);
    }

    @Test
    public void testToStringContainsKeyFields() {
        // Just checking that toString includes some important fields.
        Catch c = new Catch(
                9,
                3,
                "Perch",
                "Shoreline",
                "Jig",
                "2024-03-10",
                "Cloudy day",
                11.2,
                0.9
        );

        String text = c.toString();

        assertTrue(text.contains("9"));         // catchId
        assertTrue(text.contains("Perch"));     // speciesName
        assertTrue(text.contains("Shoreline")); // locationName
    }
}

