package edu.cvtc.hooked.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    @Test
    void testDefaultConstructorAndSettersGetters() {
        Location loc = new Location();

        loc.setLocationId(42);
        loc.setLocationName("Lake Wissota");
        loc.setState("WI");
        loc.setCreatedByUserId(1);

        assertEquals(42, loc.getLocationId());
        assertEquals("Lake Wissota", loc.getLocationName());
        assertEquals("WI", loc.getState());
        assertEquals(1, loc.getCreatedByUserId());
    }

    @Test
    void testFullConstructor() {
        Location loc = new Location(10, "Chippewa River", "WI", 2);

        assertEquals(10, loc.getLocationId());
        assertEquals("Chippewa River", loc.getLocationName());
        assertEquals("WI", loc.getState());
        assertEquals(2, loc.getCreatedByUserId());
    }

    @Test
    void testConvenienceConstructor() {
        Location loc = new Location("Lake Altoona", "WI");

        assertNull(loc.getLocationId(), "ID should be null before insert");
        assertEquals("Lake Altoona", loc.getLocationName());
        assertEquals("WI", loc.getState());
        assertNull(loc.getCreatedByUserId());
    }

    @Test
    void testEqualsAndHashCodeUseNameAndState() {
        Location loc1 = new Location("Lake Hallie", "WI");
        Location loc2 = new Location("Lake Hallie", "WI");
        Location loc3 = new Location("Lake Hallie", "MN");
        Location loc4 = new Location("Different Lake", "WI");

        // Even if IDs differ, same (locationName, state) => equal
        loc1.setLocationId(1);
        loc2.setLocationId(2);

        assertEquals(loc1, loc2);
        assertEquals(loc1.hashCode(), loc2.hashCode());

        assertNotEquals(loc1, loc3);
        assertNotEquals(loc1, loc4);
    }

    @Test
    void testToStringContainsKeyFields() {
        Location loc = new Location(5, "Lake Menomin", "WI", 3);
        String s = loc.toString();

        assertTrue(s.contains("Lake Menomin"));
        assertTrue(s.contains("WI"));
        assertTrue(s.contains("5"));
    }
}
