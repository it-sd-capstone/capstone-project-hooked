package edu.cvtc.hooked;

import edu.cvtc.hooked.model.Location;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LocationTest {

    @Test
    void testFullConstructor() {
        Location loc = new Location(5, "Lake Superior", "WI");

        assertEquals(5, loc.getLocationId());
        assertEquals("Lake Superior", loc.getLocationName());
        assertEquals("WI", loc.getState());
    }

    @Test
    void testSettersAndGetters() {
        Location loc = new Location();
        loc.setLocationId(10);
        loc.setLocationName("Mississippi River");
        loc.setState("MN");

        assertEquals(10, loc.getLocationId());
        assertEquals("Mississippi River", loc.getLocationName());
        assertEquals("MN", loc.getState());
    }

    @Test
    void testEqualityByNameAndState() {
        Location a = new Location(3, "Chippewa River", "WI");
        Location b = new Location(99, "Chippewa River", "WI");

        // Same name + same state = equal
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testInequalityDifferentNameOrState() {
        Location a = new Location(1, "Oneida Lake", "NY");
        Location b = new Location(2, "Oneida Lake", "WI"); // different state

        assertNotEquals(a, b);

        Location c = new Location(5, "Different Lake", "NY"); // different name
        assertNotEquals(a, c);
    }
}
