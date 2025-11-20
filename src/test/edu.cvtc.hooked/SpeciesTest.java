package edu.cvtc.hooked;

import edu.cvtc.hooked.model.Species;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpeciesTest {

    @Test
    public void testDefaultConstructorStartsEmpty() {
        // Just checking that the empty constructor does not prefill anything.
        Species s = new Species();

        assertNull(s.getSpeciesId());
        assertNull(s.getSpeciesName());
        assertNull(s.getLength());
        assertNull(s.getWeight());
    }

    @Test
    public void testFullConstructorSetsAllFields() {
        // This uses the constructor that takes the ID and all fields.
        // Making sure every value lands in the correct spot.
        Species s = new Species(
                10,
                "Walleye",
                22.5,
                4.7
        );

        assertEquals(10, s.getSpeciesId());
        assertEquals("Walleye", s.getSpeciesName());
        assertEquals(22.5, s.getLength());
        assertEquals(4.7, s.getWeight());
    }

    @Test
    public void testNewSpeciesConstructorHasNullId() {
        // The new species constructor should not set an ID.
        Species s = new Species(
                "Bass",
                18.0,
                3.2
        );

        assertNull(s.getSpeciesId());
        assertEquals("Bass", s.getSpeciesName());
        assertEquals(18.0, s.getLength());
        assertEquals(3.2, s.getWeight());
    }

    @Test
    public void testSettersAndGetters() {
        // Making sure all setters and getters behave correctly.
        Species s = new Species();

        s.setSpeciesId(5);
        s.setSpeciesName("Pike");
        s.setLength(30.0);
        s.setWeight(8.0);

        assertEquals(5, s.getSpeciesId());
        assertEquals("Pike", s.getSpeciesName());
        assertEquals(30.0, s.getLength());
        assertEquals(8.0, s.getWeight());
    }

    @Test
    public void testEqualsUsesSpeciesName() {
        // Two species with the same speciesName should be equal.
        Species a = new Species("Bluegill", 8.0, 0.5);
        Species b = new Species("Bluegill", 10.0, 0.7);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testNotEqualWhenSpeciesNameDiffers() {
        // Different speciesName values should not be equal.
        Species a = new Species("Crappie", 8.0, 0.5);
        Species b = new Species("Sunfish", 8.0, 0.5);

        assertNotEquals(a, b);
    }

    @Test
    public void testToStringIncludesIdAndName() {
        // toString should show useful debug info but not be overly detailed.
        Species s = new Species(
                3,
                "Trout",
                19.0,
                2.3
        );

        String text = s.toString();

        assertTrue(text.contains("3"));
        assertTrue(text.toLowerCase().contains("trout"));
    }
}


