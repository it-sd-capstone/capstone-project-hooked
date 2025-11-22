package edu.cvtc.hooked;

import edu.cvtc.hooked.model.Bait;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BaitTest {

    @Test
    void testFullConstructor() {
        Bait bait = new Bait(1, "Minnow", "Good for walleye");

        assertEquals(1, bait.getId());
        assertEquals("Minnow", bait.getName());
        assertEquals("Good for walleye", bait.getNotes());
    }

    @Test
    void testNameNotesConstructor() {
        Bait bait = new Bait("Leech", "Used often in summer");

        assertEquals("Leech", bait.getName());
        assertEquals("Used often in summer", bait.getNotes());
        assertEquals(0, bait.getId()); // default int value
    }

    @Test
    void testSetters() {
        Bait bait = new Bait();
        bait.setId(10);
        bait.setName("Nightcrawler");
        bait.setNotes("Classic worm");

        assertEquals(10, bait.getId());
        assertEquals("Nightcrawler", bait.getName());
        assertEquals("Classic worm", bait.getNotes());
    }

    @Test
    void testTwoObjectsNotEqualById() {
        Bait a = new Bait(1, "Test", "A");
        Bait b = new Bait(2, "Test", "A");

        // No custom equals method, so compare manually
        assertNotEquals(a.getId(), b.getId());
    }
}

