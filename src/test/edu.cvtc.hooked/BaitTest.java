package edu.cvtc.hooked;

import edu.cvtc.hooked.model.Bait;
import edu.cvtc.hooked.util.DbUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BaitTest {

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
    void testFullConstructor() {
        Bait bait = new Bait(1, "Minnow", "Good for walleye");

        assertEquals(1, bait.getId());
        assertEquals("Minnow", bait.getName());
        assertEquals("Good for walleye", bait.getNotes());
    }

    @Test
    void testNameNotesConstructor() {
        Bait bait = new Bait("Leech", "Used often in summer");

        assertNull(bait.getId());
        assertEquals("Leech", bait.getName());
        assertEquals("Used often in summer", bait.getNotes());
    }

    @Test
    void testSetters() {
        Bait bait = new Bait();
        bait.setId(10);
        bait.setName("Nightcrawler");
        bait.setNotes("Classic worm");

        assertEquals(10, bait.getId());
        assertEquals("Nightcrawler", bait.getName());
        assertEquals("Classic Worm", bait.getNotes());
    }

    @Test
    void testFormattingHandlesMultipleWords() {
        Bait bait = new Bait("cut bait", "smells very strong");

        assertEquals("Cut Bait", bait.getName());
        assertEquals("smells very strong", bait.getNotes());
    }
}

