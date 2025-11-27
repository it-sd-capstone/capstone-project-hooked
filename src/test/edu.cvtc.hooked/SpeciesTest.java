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
                25.0,
                12.0
        );

        assertEquals(10, s.getSpeciesId());
        assertEquals("Walleye", s.getSpeciesName());
        assertEquals(25.0, s.getLength());
        assertEquals(12.0, s.getWeight());
    }

    @Test
    public void testNewSpeciesConstructorHasNullId() {
        // The new species constructor should not set an ID.
        Species s = new Species(
                "Bass",
                20.0,
                6.0
        );

        assertNull(s.getSpeciesId());
        assertEquals("Bass", s.getSpeciesName());
        assertEquals(20.0, s.getLength());
        assertEquals(6.0, s.getWeight());
    }

    @Test
    public void testSettersAndGetters() {
        // Making sure all setters and getters behave correctly.
        Species s = new Species();

        s.setSpeciesId(5);
        s.setSpeciesName("Pike");
        s.setLength(40.0);
        s.setWeight(15.0);

        assertEquals(5, s.getSpeciesId());
        assertEquals("Pike", s.getSpeciesName());
        assertEquals(40.0, s.getLength());
        assertEquals(15.0, s.getWeight());
    }

    @Test
    public void testEqualsUsesSpeciesName() {
        // Two species with the same speciesName should be equal.
        Species a = new Species("Bluegill", 10.0, 1.0);
        Species b = new Species("Bluegill", 12.0, 1.2);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testNotEqualWhenSpeciesNameDiffers() {
        // Different speciesName values should not be equal.
        Species a = new Species("Crappie", 5.0, 1.0);
        Species b = new Species("Sunfish", 5.0, 1.0);

        assertNotEquals(a, b);
    }

    @Test
    public void testToStringIncludesIdAndName() {
        // toString should show useful debug info but not be overly detailed.
        Species s = new Species(
                3,
                "Trout",
                25.0,
                5.0
        );

        String text = s.toString();

        assertTrue(text.contains("3"));
        assertTrue(text.toLowerCase().contains("trout"));
    }
}


